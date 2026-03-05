package com.starhomes.app.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.starhomes.app.data.Screen
import com.starhomes.app.data.User
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray800
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.StarHomeTextField
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    user: User,
    onUpdateUser: (User) -> Unit,
    navigateTo: (Screen) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var password by remember { mutableStateOf("••••••••") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var showPhotoSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // URI temporária para foto da câmera
    val cameraUri = remember {
        val file = File(context.cacheDir, "temp_photo.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    // Launcher para galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { avatarUri = it }
    }

    // Launcher para câmera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) avatarUri = cameraUri
    }

    // Launcher para permissão da câmera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(cameraUri)
    }

    // Bottom Sheet para escolha de foto
    if (showPhotoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoSheet = false },
            containerColor = Gray800
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Alterar foto",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Opção: Galeria
                OutlinedButton(
                    onClick = {
                        showPhotoSheet = false
                        galleryLauncher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Gray400)
                ) {
                    Icon(
                        Icons.Default.Photo,
                        contentDescription = null,
                        tint = Blue400,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Escolher da galeria")
                }

                // Opção: Câmera
                OutlinedButton(
                    onClick = {
                        showPhotoSheet = false
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Gray400)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Blue400,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Tirar foto")
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Editar Cadastro", color = Color.White, fontSize = 22.sp,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 20.dp)
        )

        // Avatar
        AsyncImage(
            model = avatarUri ?: user.avatar,
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .border(3.dp, Blue400, CircleShape)
        )
        Spacer(Modifier.height(8.dp))

        // Botão que abre o bottom sheet
        TextButton(onClick = { showPhotoSheet = true }) {
            Text("Trocar foto", color = Blue400, fontSize = 13.sp)
        }
        Spacer(Modifier.height(16.dp))

        // Form fields
        FormLabel("Nome completo")
        StarHomeTextField(name, { name = it }, "Nome completo")
        Spacer(Modifier.height(12.dp))

        FormLabel("E-mail")
        StarHomeTextField(email, { email = it }, "E-mail")
        Spacer(Modifier.height(12.dp))

        FormLabel("Telefone")
        StarHomeTextField(phone, { phone = it }, "Telefone")
        Spacer(Modifier.height(12.dp))

        FormLabel("Senha")
        StarHomeTextField(password, { password = it }, "Senha", isPassword = true)
        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Salvar",
            onClick = {
                onUpdateUser(
                    user.copy(
                        name = name,
                        email = email,
                        phone = phone,
                        avatar = avatarUri?.toString() ?: user.avatar
                    )
                )
            }
        )
        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { navigateTo(Screen.PREFERENCES_REPORT) }) {
            Text("Relatório de Preferências", color = Blue400)
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        color = Gray400,
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}
