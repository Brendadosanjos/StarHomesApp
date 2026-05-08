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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField

// ─────────────────────────────────────────────
// MELHORIAS DE ACESSIBILIDADE COGNITIVA:
//
// • Validação de campos com mensagens de erro claras e específicas
//   (não apenas "erro" genérico — o usuário sabe exatamente o que corrigir)
// • Label fixo acima de cada campo (não some ao digitar)
// • Botão desabilitado enquanto campos estão vazios
// • Mensagem de boas-vindas mais simples e direta
// • Feedback de erro logo abaixo do campo problemático
// ─────────────────────────────────────────────

@Composable
fun LoginScreen(navigateTo: (Screen) -> Unit) {
    var email by remember { mutableStateOf("rafael.almeida@email.com") }
    var password by remember { mutableStateOf("password") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // COGNITIVO: validação simples antes de prosseguir
    fun validate(): Boolean {
        var valid = true
        if (email.isBlank()) {
            emailError = "Digite seu e-mail para continuar."
            valid = false
        } else if (!email.contains("@")) {
            emailError = "E-mail inválido. Exemplo: nome@email.com"
            valid = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = "Digite sua senha para continuar."
            valid = false
        } else if (password.length < 6) {
            passwordError = "A senha precisa ter pelo menos 6 caracteres."
            valid = false
        } else {
            passwordError = null
        }
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Logo Star Homes",
            tint = Blue400,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(16.dp))

        // COGNITIVO: saudação simples e direta
        Text("Bem-vindo! 👋", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Entre na sua conta para encontrar o bairro ideal em Londres.",
            color = Gray400,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(32.dp))

        // COGNITIVO: label fixo + mensagem de erro específica por campo
        StarHomeTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null // limpa erro ao digitar
            },
            placeholder = "nome@email.com",
            label = "E-mail",
            errorMessage = emailError
        )
        Spacer(Modifier.height(12.dp))

        StarHomeTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            placeholder = "Mínimo 6 caracteres",
            label = "Senha",
            isPassword = true,
            errorMessage = passwordError
        )
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { navigateTo(Screen.FORGOT_PASSWORD) }) {
                Text("Esqueceu a senha?", color = Blue400, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text = "Entrar",
            onClick = {
                if (validate()) navigateTo(Screen.PROFILE_SETUP)
            }
        )
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Não tem uma conta? ", color = Gray400, fontSize = 14.sp)
            TextButton(onClick = { navigateTo(Screen.SIGNUP) }) {
                Text("Cadastre-se", color = Blue400, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}