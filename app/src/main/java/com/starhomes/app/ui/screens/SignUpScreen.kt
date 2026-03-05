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
fun SignUpScreen(navigateTo: (Screen) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Logo",
            tint = Blue400,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("Crie sua conta", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        StarHomeTextField(name, { name = it }, "Nome completo")
        Spacer(Modifier.height(12.dp))
        StarHomeTextField(email, { email = it }, "E-mail")
        Spacer(Modifier.height(12.dp))
        StarHomeTextField(password, { password = it }, "Senha", isPassword = true)
        Spacer(Modifier.height(12.dp))
        StarHomeTextField(confirmPassword, { confirmPassword = it }, "Confirmar senha", isPassword = true)
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
            Text("Eu aceito os Termos e Política de Privacidade", color = Color(0xFFD1D5DB), fontSize = 13.sp)
        }
        Spacer(Modifier.height(16.dp))

        PrimaryButton(text = "Cadastrar", onClick = { navigateTo(Screen.PROFILE_SETUP) })
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Já tem uma conta? ", color = Gray400, fontSize = 14.sp)
            TextButton(onClick = { navigateTo(Screen.LOGIN) }) {
                Text("Entrar", color = Blue400, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
