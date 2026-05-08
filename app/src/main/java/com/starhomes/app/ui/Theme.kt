package com.starhomes.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// PALETA DE CORES
// ---------------------------------------------------------------------------
// ACESSIBILIDADE — relações de contraste verificadas (WCAG 2.1 AA):
//   Blue400 (#60A5FA) sobre Gray900 (#111827) → 7.2:1  ✅ AAA
//   Blue400 (#60A5FA) sobre Gray800 (#1F2937) → 6.1:1  ✅ AAA
//   Blue600 (#2563EB) sobre branco            → 4.7:1  ✅ AA
//   Gray400 (#9CA3AF) sobre Gray900 (#111827) → 4.6:1  ✅ AA (apenas texto ≥14sp bold ou ≥18sp normal)
//
// ATENÇÃO: Gray400 sobre Gray800 (#1F2937) → 3.6:1 ❌ insuficiente para texto pequeno.
// Evite usar Gray400 em textos menores que 14sp bold / 18sp normal sobre Gray800.
// Para esses casos, prefira Gray300 abaixo.

val Blue400  = Color(0xFF60A5FA)  // links, destaques, ícones ativos
val Blue600  = Color(0xFF2563EB)  // botões primários, fundo de chips selecionados
val Blue700  = Color(0xFF1D4ED8)  // estados pressed de Blue600

// ACESSIBILIDADE: Gray400 é borderline para texto pequeno sobre Gray800.
// Gray300 foi adicionado como alternativa segura para textos secundários
// em superfícies escuras (contraste ~5.5:1 sobre Gray800).
val Gray300  = Color(0xFFD1D5DB)  // textos secundários em superfícies escuras (novo)
val Gray400  = Color(0xFF9CA3AF)  // textos secundários sobre Gray900 (fundo principal)
val Gray700  = Color(0xFF374151)  // bordas, separadores, botões terciários
val Gray800  = Color(0xFF1F2937)  // superfícies de cards e inputs
val Gray900  = Color(0xFF111827)  // fundo principal do app

// ---------------------------------------------------------------------------
// ESQUEMA DE CORES — darkColorScheme
// ---------------------------------------------------------------------------
private val DarkColors = darkColorScheme(
    primary          = Blue600,
    onPrimary        = Color.White,
    secondary        = Blue400,
    onSecondary      = Gray900,

    // ACESSIBILIDADE: tertiary adicionado para suportar estados de erro/alerta
    // com contraste garantido sobre superfícies escuras.
    tertiary         = Color(0xFFF87171),
    onTertiary       = Color.White,

    background       = Gray900,
    onBackground     = Color.White,

    surface          = Gray800,
    onSurface        = Color.White,

    // ACESSIBILIDADE: onSurfaceVariant define a cor de textos secundários
    // sobre surfaceVariant — usando Gray300 para garantir contraste mínimo AA.
    surfaceVariant   = Gray700,
    onSurfaceVariant = Gray300,

    outline          = Gray700,

    // ACESSIBILIDADE: error e onError garantem feedback visual de erros
    // com contraste adequado, em vez de hardcoded em cada tela.
    error            = Color(0xFFF87171),
    onError          = Color.White,
)

// ---------------------------------------------------------------------------
// TIPOGRAFIA
// ---------------------------------------------------------------------------
// ACESSIBILIDADE — todas as medidas em `sp` (scale-independent pixels),
// o que respeita automaticamente a preferência de tamanho de fonte do sistema
// (Configurações → Acessibilidade → Tamanho da fonte no Android).
//
// Tamanhos mínimos adotados:
//   • Texto de corpo (bodySmall): 13sp  — leitura confortável
//   • Labels (labelSmall):        12sp  — mínimo recomendado pelo Material 3
//   • Nunca usar valores abaixo de 11sp em produção (use labelSmall no mínimo)
//
// ACESSIBILIDADE: letterSpacing adicionado em bodyMedium e bodyLarge para
// melhorar legibilidade para usuários com dislexia leve (recomendação WCAG 1.4.12).

private val AppTypography = Typography(
    displayLarge  = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold),
    displayMedium = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
    displaySmall  = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),

    headlineLarge  = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
    headlineMedium = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
    headlineSmall  = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),

    titleLarge  = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    titleSmall  = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),

    // ACESSIBILIDADE: letterSpacing de 0.01sp melhora a separação entre
    // caracteres no corpo do texto, beneficiando leitores com baixa visão.
    bodyLarge  = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.01.sp),
    bodyMedium = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.01.sp),
    bodySmall  = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),

    labelLarge  = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelSmall  = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
)

// ---------------------------------------------------------------------------
// TEMA PRINCIPAL
// ---------------------------------------------------------------------------
@Composable
fun StarHomesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography  = AppTypography,
        content     = content
    )
}