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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray700
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField

@Composable
fun ForgotPasswordScreen(navigateTo: (Screen) -> Unit) {
    var step by remember { mutableStateOf("request") } // request, verify, reset
    var method by remember { mutableStateOf("email") }
    var identifier by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                Text("Escolha como deseja receber o código de verificação:",
                    color = Gray400, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MethodButton("E-mail", method == "email", Modifier.weight(1f)) { method = "email" }
                    MethodButton("SMS", method == "sms", Modifier.weight(1f)) { method = "sms" }
                }
                Spacer(Modifier.height(12.dp))
                StarHomeTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    placeholder = if (method == "email") "Seu e-mail" else "Seu celular"
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton("Enviar Código", onClick = { step = "verify" })
            }

            "verify" -> {
                Text("Enviamos um código para $identifier. Insira-o abaixo:",
                    color = Gray400, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
                StarHomeTextField(code, { code = it }, "Código de 6 dígitos")
                Spacer(Modifier.height(16.dp))
                PrimaryButton("Verificar", onClick = { step = "reset" })
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { step = "request" }) {
                    Text("Reenviar código", color = Blue400)
                }
            }

            "reset" -> {
                Text("Crie uma nova senha segura para sua conta:",
                    color = Gray400, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
                StarHomeTextField(newPassword, { newPassword = it }, "Nova senha", isPassword = true)
                Spacer(Modifier.height(12.dp))
                StarHomeTextField(confirmPassword, { confirmPassword = it }, "Confirmar nova senha", isPassword = true)
                Spacer(Modifier.height(16.dp))
                PrimaryButton("Redefinir Senha", onClick = { navigateTo(Screen.LOGIN) })
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
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF2563EB) else Color(0xFF1F2937),
            contentColor = if (isSelected) Color.White else Color(0xFF9CA3AF)
        )
    ) {
        Text(label)
    }
}
