package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField

// =============================================================================
// MELHORIA DE USABILIDADE 1 — Validação de formulário (Heurística de Nielsen #5)
// -----------------------------------------------------------------------------
// ANTES: o botão "Cadastrar" navegava imediatamente para a próxima tela
// sem verificar nenhum campo. O usuário podia criar uma conta com nome
// vazio, email inválido, senhas que não conferem e sem aceitar os termos.
//
// PROBLEMA (Nielsen #5 — Prevenção de erros): erros que podem ser
// prevenidos antes de acontecer são piores do que erros com boa mensagem.
// Deixar o usuário chegar na próxima tela com dados inválidos é pior
// do que bloquear o avanço com feedback claro no momento do erro.
//
// DEPOIS: cada campo tem validação inline com mensagem de erro específica
// e em linguagem simples. O botão só navega quando todos os campos são válidos.
// Validações implementadas:
//   • Nome: obrigatório, mínimo 3 caracteres
//   • Email: obrigatório, deve conter "@" e "."
//   • Senha: obrigatória, mínimo 6 caracteres
//   • Confirmação: deve ser igual à senha
//   • Termos: obrigatório marcar antes de prosseguir
// =============================================================================

private fun isValidEmail(email: String): Boolean =
    email.contains("@") && email.contains(".") && email.length > 5

@Composable
fun SignUpScreen(navigateTo: (Screen) -> Unit) {
    var name              by remember { mutableStateOf("") }
    var email             by remember { mutableStateOf("") }
    var password          by remember { mutableStateOf("") }
    var confirmPassword   by remember { mutableStateOf("") }
    var acceptedTerms     by remember { mutableStateOf(false) }

    // Erros só são exibidos após o usuário tentar submeter o formulário,
    // evitando mensagens de erro prematuras enquanto o campo ainda está sendo preenchido.
    var submitted         by remember { mutableStateOf(false) }

    val nameError = if (submitted && name.trim().length < 3)
        "Nome deve ter pelo menos 3 caracteres" else null

    val emailError = if (submitted && !isValidEmail(email.trim()))
        "Insira um e-mail válido" else null

    val passwordError = if (submitted && password.length < 6)
        "Senha deve ter pelo menos 6 caracteres" else null

    val confirmError = if (submitted && confirmPassword != password)
        "As senhas não conferem" else null

    val termsError = if (submitted && !acceptedTerms)
        "Você deve aceitar os termos para continuar" else null

    val isFormValid = name.trim().length >= 3
            && isValidEmail(email.trim())
            && password.length >= 6
            && confirmPassword == password
            && acceptedTerms

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = null,
            tint = Blue400,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("Crie sua conta", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        // StarHomeTextField já suporta errorMessage — exibe ícone de aviso
        // + texto em vermelho abaixo do campo quando há erro.
        StarHomeTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Nome completo",
            errorMessage = nameError
        )
        Spacer(Modifier.height(12.dp))

        StarHomeTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "E-mail",
            errorMessage = emailError
        )
        Spacer(Modifier.height(12.dp))

        StarHomeTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Senha",
            isPassword = true,
            errorMessage = passwordError
        )
        Spacer(Modifier.height(12.dp))

        StarHomeTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirmar senha",
            isPassword = true,
            errorMessage = confirmError
        )
        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = Blue400)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Eu aceito os Termos e Política de Privacidade",
                color = Color(0xFFD1D5DB),
                fontSize = 13.sp
            )
        }

        // Mensagem de erro dos termos exibida abaixo do checkbox
        if (termsError != null) {
            Text(
                text = termsError,
                color = Color(0xFFF87171),
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text = "Cadastrar",
            onClick = {
                submitted = true
                if (isFormValid) {
                    navigateTo(Screen.PROFILE_SETUP)
                }
            }
        )
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Já tem uma conta? ", color = Gray400, fontSize = 14.sp)
            TextButton(onClick = { navigateTo(Screen.LOGIN) }) {
                Text("Entrar", color = Blue400, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}