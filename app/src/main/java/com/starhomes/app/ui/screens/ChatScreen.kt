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
import androidx.compose.ui.unit.dp
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Blue600
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray700
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val from: String, val text: String)

@Composable
fun ChatScreen(navigateTo: (Screen) -> Unit) {
    val messages = remember {
        mutableStateListOf(ChatMessage("bot", "Olá! Posso coletar algumas informações adicionais sobre você?"))
    }
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun handleSend() {
        if (input.isBlank()) return
        val text = input.trim()
        messages.add(ChatMessage("user", text))
        input = ""
        scope.launch {
            delay(1000)
            messages.add(ChatMessage("bot", "Entendido! Baseado em suas preferências, estou buscando os melhores bairros. Um momento..."))
            delay(2000)
            navigateTo(Screen.SEARCH_RESULTS)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(16.dp),
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
                onClick = { handleSend() },
                modifier = Modifier
                    .size(48.dp)
                    .background(Blue600, CircleShape)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White)
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(48.dp)
                    .background(Blue600, CircleShape)
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color.White)
            }
        }
    }
}
