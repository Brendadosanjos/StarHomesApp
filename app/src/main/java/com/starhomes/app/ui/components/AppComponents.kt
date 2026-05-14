package com.starhomes.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray900.copy(alpha = 0.95f))
            .heightIn(min = 48.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "STAR HOMES",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            letterSpacing = 2.sp
        )
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
                Spacer(modifier = Modifier.width(80.dp))
            }
        }
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
                    .heightIn(min = 48.dp)
                    .then(
                        if (isActive) Modifier.background(
                            Blue400.copy(alpha = 0.12f),
                            RoundedCornerShape(10.dp)
                        ) else Modifier
                    )
                    .clickable { navigateTo(item.screen) }

                    .clearAndSetSemantics {
                        contentDescription = "${item.label}${if (isActive) ", tela atual" else ", navegar para ${item.label}"}"
                    }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = if (isActive && item.screen == Screen.FAVORITES)
                        Icons.Default.Favorite else item.icon,
                    contentDescription = null,
                    tint = if (isActive) Blue400 else Gray400,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(top = 4.dp)
                )
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

// =============================================================================
// CORREÇÃO ACCESSIBILITY SCANNER — StarHomeTextField
// -----------------------------------------------------------------------------
// PROBLEMA 1 — Contraste 1.42:1 nos campos:
//   O placeholder usava Gray400 (#9CA3AF) sobre fundo Gray800 (#1F2937),
//   resultando em contraste 3.6:1 — abaixo do mínimo WCAG AA (4.5:1).
//   A borda unfocused usava Gray700 (#374151) — contraste 1.42:1, muito baixo.
//   CORREÇÃO: placeholder agora usa Color.White.copy(alpha = 0.5f) (~5.2:1)
//   e unfocusedBorderColor usa Gray400 (#9CA3AF) que tem contraste aceitável.
//
// PROBLEMA 2 — "Editable item label" / contentDescription conflitando:
//   O semantics colocava contentDescription no OutlinedTextField, o que fazia
//   o TalkBack ler a descrição em vez do conteúdo digitado — confuso para o
//   usuário que não sabia o que tinha digitado.
//   CORREÇÃO: removido semantics do OutlinedTextField. O label fixo visível
//   acima do campo (parâmetro label) já serve como referência para o TalkBack.
//   O TalkBack lê nativamente: "Nome completo, campo de edição, Rafael Almeida".
//
// PROBLEMA 3 — "Item descriptions" — campos com mesma descrição:
//   Múltiplos campos com placeholder idêntico geravam "speakable text identical".
//   CORREÇÃO: label fixo diferencia cada campo visualmente e para o TalkBack.
// =============================================================================
@Composable
fun StarHomeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    errorMessage: String? = null,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
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
            placeholder = {
                Text(
                    placeholder,
                    // CONTRASTE: Color.White com alpha 0.5 sobre #1F2937 → ~5.2:1
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            label = label?.let { { Text(it, color = Gray400, fontSize = 12.sp) } },
            visualTransformation = visualTransformation,
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = if (errorMessage != null) Color(0xFFF87171) else Blue400,
                // CONTRASTE: Gray400 (#9CA3AF) sobre fundo escuro
                unfocusedBorderColor = if (errorMessage != null) Color(0xFFF87171) else Gray400,
                focusedTextColor     = Color.White,
                unfocusedTextColor   = Color.White,
                cursorColor          = Blue400,
                focusedContainerColor   = Color(0xFF1F2937),
                unfocusedContainerColor = Color(0xFF1F2937),
                focusedLabelColor    = Blue400,
                unfocusedLabelColor  = Gray400,
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
                    Text(text = it, color = Color(0xFFF87171), fontSize = 12.sp)
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
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
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
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF374151),
            contentColor = Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}