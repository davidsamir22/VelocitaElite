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
//  Design language: deep charcoal + burnished gold + silver mist
// ─────────────────────────────────────────────────────────────

object VelocitaColors {

    // ── Backgrounds ──────────────────────────────────────────
    /** Deepest charcoal — primary app background */
    val Obsidian        = Color(0xFF0A0A0C)
    /** Mid-dark surface for cards and elevated containers */
    val CharcoalDark    = Color(0xFF141418)
    /** Slightly lighter surface for input fields / sheets */
    val CharcoalMid     = Color(0xFF1E1E24)
    /** Subtle border / divider tone */
    val CharcoalLight   = Color(0xFF2C2C36)

    // ── Gold Accents ──────────────────────────────────────────
    /** Primary brand gold — buttons, highlights, icons */
    val GoldPrimary     = Color(0xFFD4A843)
    /** Slightly muted gold for secondary emphasis */
    val GoldMuted       = Color(0xFFB8922C)
    /** Pale gold for text on dark surfaces */
    val GoldLight       = Color(0xFFE8C96A)
    /** Warm gold glow for gradient stops */
    val GoldGlow        = Color(0xFFFFE08A)

    // ── Silver / Neutral ─────────────────────────────────────
    /** Primary body text */
    val SilverLight     = Color(0xFFE0E0E8)
    /** Secondary text / captions */
    val SilverMid       = Color(0xFF9898A8)
    /** Disabled / placeholder text */
    val SilverDim       = Color(0xFF5A5A6A)

    // ── Status ───────────────────────────────────────────────
    val ErrorRed        = Color(0xFFCF4747)
    val SuccessGreen    = Color(0xFF3DAA70)
}

// ─────────────────────────────────────────────────────────────
//  MATERIAL 3 — DARK COLOR SCHEME
//  Mapped to the VelocitaColors palette for system widgets
// ─────────────────────────────────────────────────────────────
private val LuxuryDarkColorScheme = darkColorScheme(
    primary          = VelocitaColors.GoldPrimary,
    onPrimary        = VelocitaColors.Obsidian,
    primaryContainer = VelocitaColors.GoldMuted,
    secondary        = VelocitaColors.SilverMid,
    onSecondary      = VelocitaColors.Obsidian,
    background       = VelocitaColors.Obsidian,
    onBackground     = VelocitaColors.SilverLight,
    surface          = VelocitaColors.CharcoalDark,
    onSurface        = VelocitaColors.SilverLight,
    surfaceVariant   = VelocitaColors.CharcoalMid,
    onSurfaceVariant = VelocitaColors.SilverMid,
    outline          = VelocitaColors.CharcoalLight,
    error            = VelocitaColors.ErrorRed,
    onError          = VelocitaColors.SilverLight,
)

// ─────────────────────────────────────────────────────────────
//  TYPOGRAPHY
// ─────────────────────────────────────────────────────────────
val VelocitaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 48.sp,
        lineHeight   = 56.sp,
        letterSpacing = (-1).sp
    ),
    displayMedium = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 36.sp,
        lineHeight   = 44.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 28.sp,
        lineHeight   = 36.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 30.sp,
    ),
    titleLarge = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Medium,
        fontSize     = 18.sp,
        lineHeight   = 26.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Medium,
        fontSize     = 15.sp,
        lineHeight   = 22.sp,
        letterSpacing = 0.3.sp
    ),
    bodyLarge = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
    ),
    labelLarge = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 14.sp,
        lineHeight   = 18.sp,
        letterSpacing = 1.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily   = FontFamily.SansSerif,
        fontWeight   = FontWeight.Medium,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 1.2.sp
    ),
)

@Composable
fun VelocitaEliteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography  = VelocitaTypography,
        content     = content
    )
}
