package com.starhomes.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    // CORREÇÃO DE CENTRALIZAÇÃO: o problema com Row + SpaceBetween é que
    // ele distribui o espaço entre os 3 filhos (esquerda, centro, direita),
    // mas como o TextButton "Voltar" (~80dp) é mais largo que o ícone Home
    // (~32dp), o "STAR HOMES" fica deslocado para a direita visualmente.
    //
    // Solução: Box com fillMaxWidth. O título fica centralizado com
    // Alignment.Center absoluto. Botão Voltar e ícone ficam ancorados
    // nas extremidades com Alignment.CenterStart e Alignment.CenterEnd,
    // sem influenciar a posição do título.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray900.copy(alpha = 0.95f))
            .heightIn(min = 48.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Título sempre no centro absoluto da tela
        Text(
            text = "STAR HOMES",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            letterSpacing = 2.sp
        )

        // Botão Voltar ancorado à esquerda — não afeta a posição do título
        Box(modifier = Modifier.align(Alignment.CenterStart)) {
            if (showBackButton) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.semantics {
                        contentDescription = "Voltar para a tela anterior"
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Blue400,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Voltar", color = Blue400, fontSize = 14.sp)
                }
            } else {
                // Espaço reservado para manter o ícone direito no lugar
                // quando não há botão Voltar — sem afetar a altura do header.
                Spacer(modifier = Modifier.width(80.dp))
            }
        }

        // Ícone Home ancorado à direita — não afeta a posição do título
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Logo Star Homes",
            tint = Blue400,
            modifier = Modifier
                .align(Alignment.CenterEnd)
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
        NavItem(Screen.SEARCH_RESULTS, Icons.Default.Home, "Início"),
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
                    .then(
                        if (isActive) Modifier.background(
                            Blue400.copy(alpha = 0.12f),
                            RoundedCornerShape(10.dp)
                        ) else Modifier
                    )
                    .padding(vertical = 4.dp)
                    .semantics {
                        contentDescription = "${item.label}${if (isActive) ", tela atual" else ""}"
                    }
            ) {
                IconButton(
                    onClick = { navigateTo(item.screen) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isActive && item.screen == Screen.FAVORITES)
                            Icons.Default.Favorite else item.icon,
                        contentDescription = null,
                        tint = if (isActive) Blue400 else Gray400,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = item.label,
                    fontSize = 11.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
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
    isPassword: Boolean = false,
    errorMessage: String? = null,
    label: String? = null
) {
    val visualTransformation = if (isPassword)
        androidx.compose.ui.text.input.PasswordVisualTransformation()
    else
        androidx.compose.ui.text.input.VisualTransformation.None

    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            Text(
                text = it,
                color = Gray400,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Gray400) },
            visualTransformation = visualTransformation,
            isError = errorMessage != null,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = label ?: placeholder },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (errorMessage != null) Color(0xFFF87171) else Blue400,
                unfocusedBorderColor = if (errorMessage != null) Color(0xFFF87171) else Color(0xFF374151),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Blue400,
                focusedContainerColor = Color(0xFF1F2937),
                unfocusedContainerColor = Color(0xFF1F2937)
            ),
            singleLine = true
        )

        AnimatedVisibility(visible = errorMessage != null) {
            errorMessage?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Erro",
                        tint = Color(0xFFF87171),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = it,
                        color = Color(0xFFF87171),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .semantics { contentDescription = if (isLoading) "Aguarde, processando..." else text },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2563EB),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF2563EB).copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(8.dp))
            Text("Aguarde...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        } else {
            Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
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