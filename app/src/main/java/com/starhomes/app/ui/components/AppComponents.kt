package com.starhomes.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray900

@Composable
fun AppHeader(
    showBackButton: Boolean,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray900.copy(alpha = 0.95f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showBackButton) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Blue400
                )
            }
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }

        Text(
            text = "STAR HOMES",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            letterSpacing = 2.sp
        )

        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Logo",
            tint = Blue400,
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp)
        )
    }
    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
}

data class NavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@Composable
fun AppFooter(
    activeScreen: Screen,
    navigateTo: (Screen) -> Unit
) {
    val navItems = listOf(
        NavItem(Screen.SEARCH_RESULTS, Icons.Default.Home, "Home"),
        NavItem(Screen.CHAT, Icons.Default.MailOutline, "Chat"),
        NavItem(Screen.FAVORITES, Icons.Default.FavoriteBorder, "Favoritos"),
        NavItem(Screen.APPOINTMENTS, Icons.Default.DateRange, "Agenda"),
        NavItem(Screen.EDIT_PROFILE, Icons.Default.Settings, "Perfil"),
    )

    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray900.copy(alpha = 0.95f))
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        navItems.forEach { item ->
            val isActive = activeScreen == item.screen
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                IconButton(
                    onClick = { navigateTo(item.screen) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isActive && item.screen == Screen.FAVORITES)
                            Icons.Default.Favorite else item.icon,
                        contentDescription = item.label,
                        tint = if (isActive) Blue400 else Gray400,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = item.label,
                    fontSize = 10.sp,
                    color = if (isActive) Blue400 else Gray400
                )
            }
        }
    }
}

@Composable
fun StarHomeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    val visualTransformation = if (isPassword)
        androidx.compose.ui.text.input.PasswordVisualTransformation()
    else
        androidx.compose.ui.text.input.VisualTransformation.None

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Gray400) },
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue400,
            unfocusedBorderColor = Color(0xFF374151),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Blue400,
            focusedContainerColor = Color(0xFF1F2937),
            unfocusedContainerColor = Color(0xFF1F2937)
        ),
        singleLine = true
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2563EB),
            contentColor = Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF374151),
            contentColor = Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
