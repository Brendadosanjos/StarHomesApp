package com.starhomes.app.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.ProfileType
import com.starhomes.app.data.Screen
import com.starhomes.app.location.LocationManager
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Blue600
import com.starhomes.app.ui.Gray700
import com.starhomes.app.ui.Gray800
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(navigateTo: (Screen) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val locationManager = remember { LocationManager(context) }

    var selectedProfile by remember { mutableStateOf<ProfileType?>(null) }
    var address by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf(2000f) }
    var schools by remember { mutableStateOf(false) }
    var transport by remember { mutableStateOf(true) }
    var safety by remember { mutableStateOf(true) }

    var isLocating by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }

    val profileTypes = listOf(ProfileType.FAMILY, ProfileType.STUDENT, ProfileType.PROFESSIONAL)

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            isLocating = true
            locationError = null
            scope.launch {
                locationManager.getCurrentLocation().collect { location ->
                    if (location != null) {
                        val humanAddress = locationManager.getAddressFromCoordinates(
                            location.latitude,
                            location.longitude
                        )
                        address = humanAddress
                    } else {
                        locationError = "Não foi possível obter a localização."
                    }
                    isLocating = false
                }
            }
        } else {
            locationError = "Permissão de localização negada."
        }
    }

    fun requestLocation() {
        locationError = null
        if (locationManager.hasLocationPermission()) {
            isLocating = true
            scope.launch {
                locationManager.getCurrentLocation().collect { location ->
                    if (location != null) {
                        address = locationManager.getAddressFromCoordinates(
                            location.latitude,
                            location.longitude
                        )
                    } else {
                        locationError = "GPS indisponível. Verifique se está ativado."
                    }
                    isLocating = false
                }
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        Text(
            "Informe seu perfil",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(24.dp))

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
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    // ACESSIBILIDADE: semantics(mergeDescendants = true) agrupa
                    // o RadioButton + Text em um único elemento focável.
                    // Sem isso, o TalkBack focava no RadioButton isolado e
                    // anunciava apenas "Not selected" ou "checked" — sem dizer
                    // QUAL perfil estava sendo selecionado, gerando o erro
                    // "Item descriptions — speakable text identical to 2 other items".
                    // Agora anuncia: "Família, selecionado" ou "Estudante, não selecionado".
                    .semantics(mergeDescendants = true) {
                        contentDescription = if (isSelected)
                            "${type.label}, selecionado"
                        else
                            "${type.label}, toque para selecionar"
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    // ACESSIBILIDADE: onClick = null porque o Row inteiro já é
                    // clicável — evita dois elementos focáveis sobrepostos.
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = Blue400)
                )
                Spacer(Modifier.width(12.dp))
                Text(type.label, color = Color.White)
            }
        }
        Spacer(Modifier.height(20.dp))

        Text(
            "Informe seu endereço",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(8.dp))
        StarHomeTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = "Digite seu endereço ou use o GPS"
        )
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { requestLocation() },
            enabled = !isLocating,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gray700,
                disabledContainerColor = Gray700.copy(alpha = 0.5f)
            )
        ) {
            if (isLocating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Blue400,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Obtendo localização via GPS...", color = Color.White)
            } else {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Blue400,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Usar minha localização (GPS)", color = Color.White)
            }
        }

        locationError?.let { error ->
            Spacer(Modifier.height(6.dp))
            Text(error, color = Color(0xFFF87171), fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Faixa de preço máxima: £${maxPrice.toInt()}/mês",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(4.dp))
        Slider(
            value = maxPrice,
            onValueChange = { maxPrice = it },
            valueRange = 500f..5000f,
            colors = SliderDefaults.colors(thumbColor = Blue400, activeTrackColor = Blue400),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))

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
            // ACESSIBILIDADE: heightIn(min = 48.dp) garante área de toque mínima
            // de 48dp exigida pelo WCAG 2.5.5 e recomendada pelo Material Design.
            // O Scanner reportava "Touch target 32dp — consider making it 48dp or larger"
            // porque o padding(vertical = 4.dp) resultava em apenas 32dp de altura
            // (24dp do Checkbox + 8dp de padding total).
            .heightIn(min = 48.dp)
            .clickable { onCheckedChange(!checked) }
            .semantics(mergeDescendants = true) {
                contentDescription = if (checked)
                    "$label, marcado. Toque para desmarcar."
                else
                    "$label, não marcado. Toque para marcar."
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2563EB))
        )
        Spacer(Modifier.width(8.dp))
        Text(label, color = Color.White)
    }
}