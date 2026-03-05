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

@Composable
fun LoginScreen(navigateTo: (Screen) -> Unit) {
    var email by remember { mutableStateOf("rafael.almeida@email.com") }
    var password by remember { mutableStateOf("password") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Star Homes Logo",
            tint = Blue400,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("Olá! 👋", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Sou o assistente da Star Homes. Vamos encontrar o bairro ideal para você em Londres?",
            color = Gray400,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(32.dp))

        StarHomeTextField(value = email, onValueChange = { email = it }, placeholder = "E-mail")
        Spacer(Modifier.height(12.dp))
        StarHomeTextField(value = password, onValueChange = { password = it }, placeholder = "Senha", isPassword = true)
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { navigateTo(Screen.FORGOT_PASSWORD) }) {
                Text("Esqueceu a senha?", color = Blue400, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(16.dp))

        PrimaryButton(text = "Entrar", onClick = { navigateTo(Screen.PROFILE_SETUP) })
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Não tem uma conta? ", color = Gray400, fontSize = 14.sp)
            TextButton(onClick = { navigateTo(Screen.SIGNUP) }) {
                Text("Inscrever-se", color = Blue400, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
