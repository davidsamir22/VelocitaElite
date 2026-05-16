package com.velocita.elite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography

// ─────────────────────────────────────────────────────────────
//  VELOCITÀ ELITE — LUXURY DARK COLOUR PALETTE
//  Design language: Charcoal Black (#121212) + Gold (#D4AF37)
// ─────────────────────────────────────────────────────────────

object VelocitaColors {
    val Obsidian        = Color(0xFF121212)
    val CharcoalDark    = Color(0xFF1A1A1A)
    val CharcoalMid     = Color(0xFF242424)
    val CharcoalLight   = Color(0xFF333333)

    val GoldPrimary     = Color(0xFFD4AF37)
    val GoldMuted       = Color(0xFFB8922C)
    val GoldLight       = Color(0xFFE8C96A)

    val SilverLight     = Color(0xFFE0E0E8)
    val SilverMid       = Color(0xFF9898A8)
    val SilverDim       = Color(0xFF5A5A6A)

    val ErrorRed        = Color(0xFFCF4747)
}

private val LuxuryDarkColorScheme = darkColorScheme(
    primary          = VelocitaColors.GoldPrimary,
    onPrimary        = VelocitaColors.Obsidian,
    background       = VelocitaColors.Obsidian,
    onBackground     = VelocitaColors.SilverLight,
    surface          = VelocitaColors.CharcoalDark,
    onSurface        = VelocitaColors.SilverLight,
    error            = VelocitaColors.ErrorRed,
    outline          = VelocitaColors.GoldPrimary
)

val VelocitaTypography = Typography(
    displayMedium = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 36.sp,
        letterSpacing = 2.sp
    ),
    headlineMedium = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 24.sp,
    ),
    titleLarge = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 20.sp,
        letterSpacing = 1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontSize     = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 14.sp,
        letterSpacing = 2.sp
    )
)

@Composable
fun VelocitaEliteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography  = VelocitaTypography,
        content     = content
    )
}
