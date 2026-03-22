package com.starhomes.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.content.ContextCompat
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: context.cacheDir
    return File.createTempFile("STAR_HOMES_${timestamp}_", ".jpg", storageDir)
}

@Composable
fun EditProfileScreen(
    user: User,
    onUpdateUser: (User) -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var password by remember { mutableStateOf("••••••••") }
    var photoUri by remember { mutableStateOf<Any>(user.avatar) }
    var showPhotoOptions by remember { mutableStateOf(false) }
    var cameraError by remember { mutableStateOf<String?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            photoUri = tempCameraUri!!
            onUpdateUser(user.copy(avatar = tempCameraUri.toString()))
            cameraError = null
        }
        showPhotoOptions = false
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri
            onUpdateUser(user.copy(avatar = uri.toString()))
        }
        showPhotoOptions = false
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera(context, cameraLauncher) { uri -> tempCameraUri = uri }
        } else {
            cameraError = "Permissão de câmera negada."
        }
    }

    fun requestCamera() {
        cameraError = null
        val hasPerm = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPerm) {
            launchCamera(context, cameraLauncher) { uri -> tempCameraUri = uri }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
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
            text = "Editar Cadastro",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = photoUri,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Blue400, CircleShape)
            )
            IconButton(
                onClick = { showPhotoOptions = !showPhotoOptions; cameraError = null },
                modifier = Modifier.size(34.dp)
            ) {
                Surface(shape = CircleShape, color = Blue400, modifier = Modifier.size(34.dp)) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Trocar foto",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }

        if (showPhotoOptions) {
            Spacer(Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Gray800),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(220.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = { requestCamera() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null,
                                tint = Blue400, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(text = "Tirar Foto", color = Color.White, fontSize = 15.sp)
                        }
                    }
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    TextButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Photo, contentDescription = null,
                                tint = Blue400, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(text = "Escolher da Galeria", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        cameraError?.let { error ->
            Spacer(Modifier.height(6.dp))
            Text(text = error, color = Color(0xFFF87171), fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

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
            onClick = { onUpdateUser(user.copy(name = name, email = email, phone = phone)) }
        )
        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { navigateTo(Screen.PREFERENCES_REPORT) }) {
            Text(text = "Relatório de Preferências", color = Blue400)
        }
    }
}

private fun launchCamera(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Uri>,
    onUriCreated: (Uri) -> Unit
) {
    try {
        val file = createImageFile(context)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        onUriCreated(uri)
        launcher.launch(uri)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        color = Gray400,
        fontSize = 13.sp,
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    )
}