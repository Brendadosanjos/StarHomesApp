package com.starhomes.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.ProfileType
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Blue600
import com.starhomes.app.ui.Gray700
import com.starhomes.app.ui.Gray800
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField

@Composable
fun ProfileSetupScreen(navigateTo: (Screen) -> Unit) {
    var selectedProfile by remember { mutableStateOf<ProfileType?>(null) }
    var address by remember { mutableStateOf("") }
    var minPrice by remember { mutableStateOf(1000f) }
    var maxPrice by remember { mutableStateOf(2000f) }
    var schools by remember { mutableStateOf(false) }
    var transport by remember { mutableStateOf(true) }
    var safety by remember { mutableStateOf(true) }

    val profileTypes = listOf(ProfileType.FAMILY, ProfileType.STUDENT, ProfileType.PROFESSIONAL)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text("Informe seu perfil", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(24.dp))

        // Profile type
        Text("Perfil", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        profileTypes.forEach { type ->
            val isSelected = selectedProfile == type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Blue600.copy(alpha = 0.3f) else Gray800)
                    .border(
                        width = if (isSelected) 1.dp else 0.dp,
                        color = if (isSelected) Blue400 else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { selectedProfile = type }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { selectedProfile = type },
                    colors = RadioButtonDefaults.colors(selectedColor = Blue400)
                )
                Spacer(Modifier.width(12.dp))
                Text(type.label, color = Color.White)
            }
        }
        Spacer(Modifier.height(20.dp))

        // Address
        Text("Informe seu endereço", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        StarHomeTextField(value = address, onValueChange = { address = it }, placeholder = "Digite seu endereço manualmente")
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { address = "51.5074, -0.1278" },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Gray700)
        ) {
            Text("📍 Usar minha localização", color = Color.White)
        }
        Spacer(Modifier.height(20.dp))

        // Price range
        Text("Faixa de preço: £${minPrice.toInt()} - £${maxPrice.toInt()}/mês",
            color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))
        Slider(
            value = maxPrice,
            onValueChange = { maxPrice = it },
            valueRange = 500f..5000f,
            colors = SliderDefaults.colors(thumbColor = Blue400, activeTrackColor = Blue400),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))

        // Priorities
        Text("Prioridades", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        CheckItem("Escolas próximas", schools) { schools = it }
        CheckItem("Transporte público", transport) { transport = it }
        CheckItem("Segurança", safety) { safety = it }
        Spacer(Modifier.height(32.dp))

        PrimaryButton(
            text = "Continuar para o Chat",
            onClick = { navigateTo(Screen.CHAT) }
        )
    }
}

@Composable
private fun CheckItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2563EB))
        )
        Spacer(Modifier.width(8.dp))
        Text(label, color = Color.White)
    }

}

