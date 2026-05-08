package com.starhomes.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.starhomes.app.AppViewModel
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Blue600
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray700

// =============================================================================
// REFATORAÇÃO 3 — Lógica de chat extraída para o AppViewModel (SOLID - SRP)
// -----------------------------------------------------------------------------
// ANTES: handleSend() com coroutine, delay e navigateTo viviam dentro do
// Composable, misturando apresentação com lógica de negócio.
//
// DEPOIS: o Composable apenas:
//   1. Observa vm.chatMessages para renderizar as bolhas
//   2. Observa vm.chatNavigation para saber quando navegar
//   3. Chama vm.handleChatSend(input) ao tocar em Enviar
//
// Benefícios:
//   • ChatScreen não tem mais lógica assíncrona — só UI
//   • handleChatSend() pode ser testado unitariamente sem Compose
//   • A navegação é consumida uma única vez (onChatNavigationConsumed),
//     evitando renavegações acidentais em recomposições
// =============================================================================

@Composable
fun ChatScreen(
    navigateTo: (Screen) -> Unit,
    vm: AppViewModel = viewModel()
) {
    val messages = vm.chatMessages
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Observa o StateFlow de navegação do ViewModel.
    // collectAsState() garante que só reage a mudanças reais de valor.
    val chatNavigation by vm.chatNavigation.collectAsState()

    // Efeito colateral: quando o ViewModel sinaliza navegação, executa
    // uma única vez e notifica o ViewModel para resetar o estado.
    LaunchedEffect(chatNavigation) {
        chatNavigation?.let { screen ->
            vm.onChatNavigationConsumed()
            navigateTo(screen)
        }
    }

    // Auto-scroll para a última mensagem sempre que a lista cresce.
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                // ChatScreen gerencia seu próprio padding horizontal agora que
                // o MainActivity não injeta mais padding externo nesta tela.
                .padding(horizontal = 20.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isBot = msg.from == "bot"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isBot) Arrangement.Start else Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .background(
                                color = if (isBot) Gray700 else Blue600,
                                shape = RoundedCornerShape(
                                    topStart = 16.dp, topEnd = 16.dp,
                                    bottomStart = if (isBot) 4.dp else 16.dp,
                                    bottomEnd = if (isBot) 16.dp else 4.dp
                                )
                            )
                            .padding(12.dp)
                            // ACESSIBILIDADE: anuncia quem enviou cada mensagem.
                            .semantics {
                                contentDescription =
                                    "${if (isBot) "Star Homes" else "Você"}: ${msg.text}"
                            }
                    ) {
                        Text(msg.text, color = Color.White)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Uma resposta", color = Gray400) },
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue400,
                    unfocusedBorderColor = Gray700,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Blue400,
                    focusedContainerColor = Color(0xFF1F2937),
                    unfocusedContainerColor = Color(0xFF1F2937)
                ),
                singleLine = true
            )
            IconButton(
                onClick = {
                    // REFATORAÇÃO 3: apenas delega ao ViewModel e limpa o input.
                    // Sem coroutines, sem delays, sem navegação aqui.
                    vm.handleChatSend(input)
                    input = ""
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(Blue600, CircleShape)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar mensagem", tint = Color.White)
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(48.dp)
                    .background(Blue600, CircleShape)
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Gravar mensagem de voz", tint = Color.White)
            }
        }
    }
}