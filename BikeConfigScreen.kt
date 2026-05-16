package com.velocita.elite.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel
import kotlin.math.sin

// ─────────────────────────────────────────────────────────────
//  SCREEN 5 — BIKE CONFIGURATION
//
//  Detailed view of one motorcycle model.
//  Unique layout elements:
//    • Animated oscilloscope-style wave behind the hero area
//    • Large stat "dials" arranged in a 2×3 grid
//    • Colour swatch selector (visual only, no state)
//    • Full prose description
//    • CTA buttons: Configure · Concierge
//
//  Background: deep radial gradient tinted with the bike's own
//  brand colour — every model feels visually distinct.
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeConfigScreen(
    bikeId          : Int,
    viewModel       : MainViewModel,
    onBack          : () -> Unit,
    onBookConcierge : () -> Unit
) {
    // Look up the bike from the catalogue; guard against invalid id
    val bike = viewModel.getBikeById(bikeId)

    if (bike == null) {
        // Fallback: should not occur in normal flow
        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Model not found", color = VelocitaColors.ErrorRed)
        }
        return
    }

    // Resolve the bike's brand colour
    val bikeColor = remember(bike.colorHex) {
        try { Color(android.graphics.Color.parseColor(bike.colorHex)) }
        catch (e: Exception) { VelocitaColors.GoldPrimary }
    }

    // ── Animated wave progress (0 → 2π, looping) ─────────────
    val waveProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        waveProgress.animateTo(
            targetValue   = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation  = tween(2800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    // ── Colour swatch options ─────────────────────────────────
    val swatchColors = listOf(
        bikeColor,
        Color(0xFF1A1A1A),   // Stealth Black
        Color(0xFFC0C0C0),   // Titanium Silver
        Color(0xFFFFFFFF),   // Pearl White
    )
    var selectedSwatch by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background ────────────────────────────────────────
        // Unique: radial gradient tinted with the bike's colour
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colorStops = arrayOf(
                            0.0f to bikeColor.copy(alpha = 0.18f),
                            0.45f to Color(0xFF0E0E12),
                            1.0f to Color(0xFF0A0A0C)
                        ),
                        center = Offset(Float.MAX_VALUE, 0f),
                        radius = 1200f
                    )
                )
        )

        // Animated oscilloscope wave panel (Canvas)
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.TopCenter)
        ) {
            val w     = size.width
            val midY  = size.height * 0.6f
            val amp   = size.height * 0.25f
            val path  = Path()
            var first = true

            // Draw a sine wave across the full width
            for (px in 0..w.toInt() step 2) {
                val t = px / w.toFloat()
                val y = midY + sin(t * 3 * Math.PI + waveProgress.value).toFloat() * amp
                if (first) { path.moveTo(px.toFloat(), y); first = false }
                else        { path.lineTo(px.toFloat(), y) }
            }

            // Stroke the wave in the bike's colour
            drawPath(
                path  = path,
                color = bikeColor.copy(alpha = 0.35f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )

            // Filled glow beneath the wave
            path.lineTo(w, size.height)
            path.lineTo(0f, size.height)
            path.close()
            drawPath(
                path  = path,
                brush = Brush.verticalGradient(
                    listOf(
                        bikeColor.copy(alpha = 0.10f),
                        Color.Transparent
                    )
                )
            )
        }

        // ── Scrollable content ────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top bar ───────────────────────────────────────
            TopAppBar(
                title        = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint               = VelocitaColors.SilverLight
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onBookConcierge) {
                        Icon(
                            imageVector        = Icons.Default.HeadsetMic,
                            contentDescription = "Concierge",
                            tint               = VelocitaColors.GoldPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
            )

            // ── Hero area: Brand + Model ──────────────────────
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 60.dp),  // offset below wave
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text  = bike.brand.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color        = bikeColor,
                        letterSpacing = 4.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = bike.name,
                    style = MaterialTheme.typography.displayMedium.copy(
                        color      = VelocitaColors.SilverLight,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text  = bike.priceDisplay,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color      = VelocitaColors.GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Performance stat grid ─────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text  = "PERFORMANCE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color        = VelocitaColors.SilverMid,
                        letterSpacing = 3.sp
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 2-column grid of stat dials
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatDial(
                        modifier = Modifier.weight(1f),
                        value    = "${bike.horsePower}",
                        unit     = "HP",
                        label    = "Peak Power",
                        color    = bikeColor
                    )
                    StatDial(
                        modifier = Modifier.weight(1f),
                        value    = "${bike.engineCC}",
                        unit     = "CC",
                        label    = "Displacement",
                        color    = VelocitaColors.GoldPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatDial(
                        modifier = Modifier.weight(1f),
                        value    = "${bike.topSpeedKph}",
                        unit     = "KPH",
                        label    = "Top Speed",
                        color    = VelocitaColors.SilverMid
                    )
                    StatDial(
                        modifier = Modifier.weight(1f),
                        value    = "${bike.weightKg}",
                        unit     = "KG",
                        label    = "Kerb Weight",
                        color    = VelocitaColors.SilverMid
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Colour selector ───────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text  = "LIVERY",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color        = VelocitaColors.SilverMid,
                        letterSpacing = 3.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    swatchColors.forEachIndexed { index, color ->
                        ColorSwatch(
                            color      = color,
                            isSelected = selectedSwatch == index,
                            onClick    = { selectedSwatch = index }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text  = when (selectedSwatch) {
                        0    -> "Brand Signature"
                        1    -> "Stealth Black"
                        2    -> "Titanium Silver"
                        else -> "Pearl White"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.SilverMid
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Description ───────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text  = "THE MACHINE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color        = VelocitaColors.SilverMid,
                        letterSpacing = 3.sp
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Left-bordered description block
                Row {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(IntrinsicSize.Max)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        bikeColor.copy(alpha = 0.7f),
                                        bikeColor.copy(alpha = 0.1f)
                                    )
                                )
                            )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text      = bike.description,
                        style     = MaterialTheme.typography.bodyLarge.copy(
                            color      = VelocitaColors.SilverMid,
                            lineHeight = 28.sp
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── CTA buttons ───────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary: Reserve this model
                Button(
                    onClick = onBookConcierge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VelocitaColors.GoldPrimary,
                        contentColor   = VelocitaColors.Obsidian
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text  = "RESERVE THIS MODEL",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 2.sp
                        )
                    )
                }

                // Secondary: Book private viewing
                OutlinedButton(
                    onClick  = onBookConcierge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    border   = androidx.compose.foundation.BorderStroke(
                        1.dp, VelocitaColors.CharcoalLight
                    ),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        contentColor = VelocitaColors.SilverMid
                    ),
                    shape    = RoundedCornerShape(4.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier           = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text  = "BOOK PRIVATE VIEWING",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  STAT DIAL
// ─────────────────────────────────────────────────────────────

/**
 * A single performance metric displayed in a bordered card.
 *
 * @param value The numeric value string (e.g. "240").
 * @param unit  Unit label shown in smaller text (e.g. "HP").
 * @param label Descriptive label below (e.g. "Peak Power").
 * @param color Accent colour for the value and border.
 */
@Composable
private fun StatDial(
    value    : String,
    unit     : String,
    label    : String,
    color    : Color,
    modifier : Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(8.dp),
        color    = VelocitaColors.CharcoalDark
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    color.copy(alpha = 0.25f),
                    RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            // Subtle corner glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawCircle(
                            color  = color.copy(alpha = 0.06f),
                            radius = size.minDimension * 0.7f,
                            center = Offset(0f, 0f)
                        )
                    }
            )

            Column(horizontalAlignment = Alignment.Start) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text  = value,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color      = color,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text     = unit,
                        style    = MaterialTheme.typography.labelLarge.copy(
                            color        = color.copy(alpha = 0.7f),
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color        = VelocitaColors.SilverDim,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COLOUR SWATCH
// ─────────────────────────────────────────────────────────────

/**
 * A circular colour picker swatch.
 *
 * @param color      The colour this swatch represents.
 * @param isSelected Whether this swatch is currently selected.
 * @param onClick    Called when the swatch is tapped.
 */
@Composable
private fun ColorSwatch(
    color      : Color,
    isSelected : Boolean,
    onClick    : () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .border(
                width  = if (isSelected) 2.dp else 1.dp,
                color  = if (isSelected) VelocitaColors.GoldPrimary
                         else VelocitaColors.CharcoalLight,
                shape  = RoundedCornerShape(50)
            )
            .padding(4.dp)
            .background(color = color, shape = RoundedCornerShape(50))
            .clickable(onClick = onClick)
    )
}
