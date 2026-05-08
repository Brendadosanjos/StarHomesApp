package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray700
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField

// =============================================================================
// MELHORIA DE USABILIDADE 2 — Validação por etapa no fluxo de recuperação
// -----------------------------------------------------------------------------
// ANTES: cada etapa avançava sem validar nada.
//   • "Enviar Código": avançava com identifier vazio
//   • "Verificar": avançava com código vazio ou incompleto
//   • "Redefinir Senha": avançava mesmo com senhas não conferindo
//
// PROBLEMA (Nielsen #5 — Prevenção de erros): o usuário podia completar
// todo o fluxo sem perceber que inseriu dados inválidos, chegando a uma
// tela de sucesso falso. Isso quebra a confiança no app.
//
// DEPOIS: cada etapa valida seu próprio conjunto de campos antes de avançar,
// com mensagens de erro específicas e em linguagem direta.
// =============================================================================

private fun isValidEmailOrPhone(value: String, method: String): Boolean =
    if (method == "email") value.contains("@") && value.contains(".")
    else value.replace(Regex("[^0-9]"), "").length >= 10

@Composable
fun ForgotPasswordScreen(navigateTo: (Screen) -> Unit) {
    var step            by remember { mutableStateOf("request") }
    var method          by remember { mutableStateOf("email") }
    var identifier      by remember { mutableStateOf("") }
    var code            by remember { mutableStateOf("") }
    var newPassword     by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var submitted       by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Home, contentDescription = null, tint = Blue400, modifier = Modifier.size(60.dp))
        Spacer(Modifier.height(8.dp))
        Text("Recuperar Senha", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        when (step) {
            "request" -> {
                Text(
                    "Escolha como deseja receber o código de verificação:",
                    color = Gray400,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MethodButton("E-mail", method == "email", Modifier.weight(1f)) { method = "email" }
                    MethodButton("SMS", method == "sms", Modifier.weight(1f)) { method = "sms" }
                }
                Spacer(Modifier.height(12.dp))

                val identifierError = if (submitted && !isValidEmailOrPhone(identifier.trim(), method))
                    if (method == "email") "Insira um e-mail válido" else "Insira um número com pelo menos 10 dígitos"
                else null

                StarHomeTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    placeholder = if (method == "email") "Seu e-mail" else "Seu celular",
                    errorMessage = identifierError
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton("Enviar Código", onClick = {
                    submitted = true
                    if (isValidEmailOrPhone(identifier.trim(), method)) {
                        submitted = false
                        step = "verify"
                    }
                })
            }

            "verify" -> {
                Text(
                    "Enviamos um código para $identifier. Insira-o abaixo:",
                    color = Gray400,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val codeError = if (submitted && code.trim().length < 6)
                    "O código deve ter 6 dígitos" else null

                StarHomeTextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = "Código de 6 dígitos",
                    errorMessage = codeError
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton("Verificar", onClick = {
                    submitted = true
                    if (code.trim().length >= 6) {
                        submitted = false
                        step = "reset"
                    }
                })
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { submitted = false; step = "request" }) {
                    Text("Reenviar código", color = Blue400)
                }
            }

            "reset" -> {
                Text(
                    "Crie uma nova senha segura para sua conta:",
                    color = Gray400,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val passwordError = if (submitted && newPassword.length < 6)
                    "Senha deve ter pelo menos 6 caracteres" else null

                val confirmError = if (submitted && confirmPassword != newPassword)
                    "As senhas não conferem" else null

                StarHomeTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = "Nova senha",
                    isPassword = true,
                    errorMessage = passwordError
                )
                Spacer(Modifier.height(12.dp))
                StarHomeTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Confirmar nova senha",
                    isPassword = true,
                    errorMessage = confirmError
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton("Redefinir Senha", onClick = {
                    submitted = true
                    if (newPassword.length >= 6 && confirmPassword == newPassword) {
                        navigateTo(Screen.LOGIN)
                    }
                })
            }
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = { navigateTo(Screen.LOGIN) }) {
            Text("Voltar para o login", color = Gray400, fontSize = 13.sp)
        }
    }
}

@Composable
private fun MethodButton(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.semantics {
            contentDescription = if (isSelected)
                "$label, selecionado. Toque para manter."
            else
                "$label, não selecionado. Toque para selecionar."
            role = Role.RadioButton
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF2563EB) else Color(0xFF1F2937),
            contentColor = if (isSelected) Color.White else Color(0xFF9CA3AF)
        )
    ) {
        Text(label)
    }
}