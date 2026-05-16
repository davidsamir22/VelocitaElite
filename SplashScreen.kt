package com.velocita.elite.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────
//  SCREEN 1 — IGNITION SPLASH
//
//  Displays an animated entry sequence:
//    Phase 1 (0–600 ms)  : Background gradient fades in
//    Phase 2 (600–1400 ms): Bike silhouette draws in via path
//    Phase 3 (1400–2000 ms): Brand name fades + scales up
//    Phase 4 (2000–2800 ms): Tagline fades in
//    Phase 5 (3000 ms)   : Auto-transitions to Showroom
//
//  Background: concentric radial sweep + diagonal speed lines
//  (pure Canvas — no image assets required)
// ─────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {

    // ── Animation states ─────────────────────────────────────

    /** Controls the overall reveal timeline (0f → 1f over 2.4 s). */
    val revealProgress = remember { Animatable(0f) }

    /** Pulsing glow ring behind the silhouette. */
    val glowPulse = remember { Animatable(0.8f) }

    /** Rotation of the decorative speed-ring. */
    val ringRotation = remember { Animatable(0f) }

    // ── Launch animation sequence ─────────────────────────────
    LaunchedEffect(Unit) {

        // Start the background ring rotation (continuous)
        ringRotation.animateTo(
            targetValue   = 360f,
            animationSpec = infiniteRepeatable(
                animation  = tween(durationMillis = 4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(Unit) {

        // Delay 200 ms before revealing content
        delay(200)

        // Animate main reveal from 0 → 1 over 2000 ms
        revealProgress.animateTo(
            targetValue   = 1f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )

        // Hold for a moment, then transition
        delay(800)
        onAnimationFinished()
    }

    LaunchedEffect(Unit) {
        // Slow glow pulse (0.8 → 1.1 and back)
        glowPulse.animateTo(
            targetValue   = 1.1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Derived alpha / scale values from the single revealProgress
    val backgroundAlpha by remember { derivedStateOf { revealProgress.value.coerceIn(0f, 1f) } }
    val silhouetteAlpha by remember {
        derivedStateOf { ((revealProgress.value - 0.3f) / 0.4f).coerceIn(0f, 1f) }
    }
    val brandAlpha by remember {
        derivedStateOf { ((revealProgress.value - 0.6f) / 0.25f).coerceIn(0f, 1f) }
    }
    val taglineAlpha by remember {
        derivedStateOf { ((revealProgress.value - 0.8f) / 0.2f).coerceIn(0f, 1f) }
    }
    val brandScale by remember {
        derivedStateOf { 0.85f + brandAlpha * 0.15f }
    }

    // ── Root container ────────────────────────────────────────
    Box(
        modifier            = Modifier.fillMaxSize(),
        contentAlignment    = Alignment.Center
    ) {

        // ── Layer 1: Deep Background Canvas ──────────────────
        // Renders: radial gradient, diagonal speed lines, rotating ring
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(backgroundAlpha)
        ) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Radial gradient — deep obsidian centre bleeding to near-black edge
            drawRect(
                brush = Brush.radialGradient(
                    colors     = listOf(
                        Color(0xFF1A1510),  // warm near-black centre
                        Color(0xFF0A0A0C),  // cold obsidian edge
                    ),
                    center = Offset(cx, cy * 0.8f),
                    radius = h * 0.72f
                ),
                size = size
            )

            // Gold accent diagonal band (top-left to mid)
            drawRect(
                brush = Brush.linearGradient(
                    colors      = listOf(
                        VelocitaColors.GoldPrimary.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end   = Offset(w * 0.6f, h * 0.4f)
                ),
                size = size
            )

            // Horizontal speed lines — evenly spaced, subtle
            val lineColor = VelocitaColors.GoldPrimary.copy(alpha = 0.05f)
            val lineCount = 12
            for (i in 0..lineCount) {
                val y = h * 0.25f + (h * 0.5f / lineCount) * i
                drawLine(
                    color       = lineColor,
                    start       = Offset(w * 0.05f, y),
                    end         = Offset(w * 0.95f, y),
                    strokeWidth = if (i % 3 == 0) 1.2f else 0.5f
                )
            }

            // Rotating decorative ring
            rotate(degrees = ringRotation.value, pivot = Offset(cx, cy * 0.8f)) {
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Transparent,
                            VelocitaColors.GoldPrimary.copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        center = Offset(cx, cy * 0.8f)
                    ),
                    radius = h * 0.28f,
                    center = Offset(cx, cy * 0.8f),
                    style  = Stroke(width = 1.5f)
                )
            }

            // Outer static ring
            drawCircle(
                color  = VelocitaColors.GoldPrimary.copy(alpha = 0.12f),
                radius = h * 0.32f,
                center = Offset(cx, cy * 0.8f),
                style  = Stroke(width = 0.8f)
            )
        }

        // ── Layer 2: Bike Silhouette (Canvas path) ────────────
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .offset(y = (-40).dp)
                .alpha(silhouetteAlpha)
                .scale(glowPulse.value * 0.99f + 0.01f)
        ) {
            val w  = size.width
            val h  = size.height
            val cx = w / 2f
            val cy = h / 2f

            // ── Glow backdrop ──
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        VelocitaColors.GoldPrimary.copy(alpha = 0.18f * glowPulse.value),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = h * 0.85f
                ),
                radius = h * 0.85f,
                center = Offset(cx, cy)
            )

            // ── Simplified superbike silhouette via Path ──────
            // Scale factor so the silhouette fits any screen width
            val scale = w / 420f

            translate(left = cx - 210f * scale, top = cy - 80f * scale) {

                val gold   = VelocitaColors.GoldPrimary
                val silver = VelocitaColors.SilverMid.copy(alpha = 0.7f)

                // --- Body / fairing ---
                val body = Path().apply {
                    moveTo(80f  * scale, 100f * scale)
                    cubicTo(
                        90f * scale, 50f * scale,
                        160f * scale, 30f * scale,
                        220f * scale, 35f * scale
                    )
                    cubicTo(
                        280f * scale, 40f * scale,
                        320f * scale, 55f * scale,
                        340f * scale, 80f * scale
                    )
                    lineTo(360f * scale, 110f * scale)
                    cubicTo(
                        340f * scale, 120f * scale,
                        300f * scale, 125f * scale,
                        260f * scale, 120f * scale
                    )
                    lineTo(150f * scale, 120f * scale)
                    cubicTo(
                        120f * scale, 118f * scale,
                        90f  * scale, 115f * scale,
                        80f  * scale, 100f * scale
                    )
                    close()
                }
                drawPath(
                    path  = body,
                    brush = Brush.linearGradient(
                        colors = listOf(gold, VelocitaColors.GoldMuted),
                        start  = Offset(80f * scale, 30f * scale),
                        end    = Offset(360f * scale, 130f * scale)
                    )
                )

                // --- Seat hump ---
                val seat = Path().apply {
                    moveTo(260f * scale, 40f * scale)
                    cubicTo(
                        290f * scale, 30f * scale,
                        330f * scale, 28f * scale,
                        355f * scale, 55f * scale
                    )
                    cubicTo(
                        340f * scale, 60f * scale,
                        300f * scale, 65f * scale,
                        260f * scale, 55f * scale
                    )
                    close()
                }
                drawPath(
                    path  = seat,
                    color = VelocitaColors.GoldMuted.copy(alpha = 0.8f)
                )

                // --- Front fork ---
                val fork = Path().apply {
                    moveTo(95f * scale, 85f * scale)
                    lineTo(60f * scale, 140f * scale)
                    lineTo(75f * scale, 142f * scale)
                    lineTo(108f * scale, 90f * scale)
                    close()
                }
                drawPath(path = fork, color = silver)

                // --- Front wheel (circle) ---
                drawCircle(
                    color  = silver,
                    radius = 42f * scale,
                    center = Offset(62f * scale, 172f * scale),
                    style  = Stroke(width = 5f * scale)
                )
                // Wheel spokes
                for (angle in 0..330 step 30) {
                    val rad = Math.toRadians(angle.toDouble())
                    drawLine(
                        color       = silver.copy(alpha = 0.5f),
                        start       = Offset(62f * scale, 172f * scale),
                        end         = Offset(
                            62f * scale + (38f * Math.cos(rad) * scale).toFloat(),
                            172f * scale + (38f * Math.sin(rad) * scale).toFloat()
                        ),
                        strokeWidth = 1.5f * scale
                    )
                }

                // --- Swing arm ---
                val arm = Path().apply {
                    moveTo(310f * scale, 115f * scale)
                    lineTo(360f * scale, 155f * scale)
                    lineTo(368f * scale, 148f * scale)
                    lineTo(318f * scale, 108f * scale)
                    close()
                }
                drawPath(path = arm, color = silver)

                // --- Rear wheel ---
                drawCircle(
                    color  = silver,
                    radius = 48f * scale,
                    center = Offset(360f * scale, 172f * scale),
                    style  = Stroke(width = 6f * scale)
                )
                for (angle in 0..330 step 30) {
                    val rad = Math.toRadians(angle.toDouble())
                    drawLine(
                        color       = silver.copy(alpha = 0.5f),
                        start       = Offset(360f * scale, 172f * scale),
                        end         = Offset(
                            360f * scale + (44f * Math.cos(rad) * scale).toFloat(),
                            172f * scale + (44f * Math.sin(rad) * scale).toFloat()
                        ),
                        strokeWidth = 1.8f * scale
                    )
                }

                // --- Exhaust can ---
                val exhaust = Path().apply {
                    moveTo(330f * scale, 130f * scale)
                    lineTo(410f * scale, 145f * scale)
                    lineTo(410f * scale, 155f * scale)
                    lineTo(330f * scale, 140f * scale)
                    close()
                }
                drawPath(
                    path  = exhaust,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            VelocitaColors.GoldLight,
                            VelocitaColors.GoldMuted
                        ),
                        start = Offset(330f * scale, 135f * scale),
                        end   = Offset(410f * scale, 150f * scale)
                    )
                )

                // --- Headlight ---
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),
                            VelocitaColors.GoldLight.copy(alpha = 0.4f),
                            Color.Transparent
                        ),
                        center = Offset(82f * scale, 72f * scale),
                        radius = 22f * scale
                    ),
                    topLeft = Offset(62f * scale, 58f * scale),
                    size    = androidx.compose.ui.geometry.Size(40f * scale, 28f * scale)
                )
            }
        }

        // ── Layer 3: Brand Text ───────────────────────────────
        Column(
            modifier           = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
                .alpha(brandAlpha)
                .scale(brandScale),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand name — large serif display
            Text(
                text      = "VELOCITÀ",
                style     = MaterialTheme.typography.displayMedium.copy(
                    color        = VelocitaColors.GoldPrimary,
                    letterSpacing = 8.sp,
                    fontWeight   = FontWeight.Bold
                ),
                textAlign  = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text  = "E  L  I  T  E",
                style = MaterialTheme.typography.labelLarge.copy(
                    color        = VelocitaColors.SilverMid,
                    letterSpacing = 10.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Decorative gold divider line
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                VelocitaColors.GoldPrimary,
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tagline — fades in slightly later
            Text(
                text      = "Purveyors of Extraordinary Machines",
                style     = MaterialTheme.typography.bodyMedium.copy(
                    color        = VelocitaColors.SilverMid,
                    letterSpacing = 1.5.sp
                ),
                textAlign  = TextAlign.Center,
                modifier   = Modifier.alpha(taglineAlpha)
            )
        }
    }
}
