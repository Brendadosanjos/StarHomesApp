package com.starhomes.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Blue400 = Color(0xFF60A5FA)
val Blue600 = Color(0xFF2563EB)
val Blue700 = Color(0xFF1D4ED8)
val Gray700 = Color(0xFF374151)
val Gray800 = Color(0xFF1F2937)
val Gray900 = Color(0xFF111827)
val Gray400 = Color(0xFF9CA3AF)

private val DarkColors = darkColorScheme(
    primary = Blue600,
    onPrimary = Color.White,
    secondary = Blue400,
    background = Gray900,
    surface = Gray800,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Gray700,
    outline = Gray700
)

@Composable
fun StarHomesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
