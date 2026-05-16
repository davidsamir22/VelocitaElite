package com.velocita.elite.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel

// ─────────────────────────────────────────────────────────────
//  SCREEN 3 — OWNER LOGIN
//
//  A minimal, sleek authentication screen for returning clients.
//  Features:
//   • Real-time inline error feedback as the user types
//   • Password visibility toggle
//   • Animated error shake (visual cue on failed submit)
//   • Background: vertical dark gradient + subtle horizontal
//     scan-line pattern unique to this screen
// ─────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(
    viewModel              : MainViewModel,
    onLoggedIn             : () -> Unit,
    onNavigateToEnrollment : () -> Unit
) {
    val focusManager = LocalFocusManager.current

    // Password field visibility state
    var passwordVisible by remember { mutableStateOf(false) }

    // Controls the horizontal "shake" animation triggered on failed login
    val shakeOffset = remember { Animatable(0f) }

    /**
     * Triggers a rapid left-right shake animation to signal
     * failed validation — provides haptic-style visual feedback.
     */
    suspend fun triggerShake() {
        val shakeSpec = tween<Float>(durationMillis = 50, easing = LinearEasing)
        for (i in 0..3) {
            shakeOffset.animateTo( 10f, shakeSpec)
            shakeOffset.animateTo(-10f, shakeSpec)
        }
        shakeOffset.animateTo(0f, shakeSpec)
    }

    val coroutineScope = rememberCoroutineScope()

    // ── Root container ────────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background: vertical scan-line gradient ───────────
        // Unique to the Login screen — cool blue-grey undertone
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF0D0D12),
                            0.5f to Color(0xFF111116),
                            1.0f to Color(0xFF090909)
                        )
                    )
                )
        )

        // Subtle scan-line overlay — very faint horizontal rules
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val scanColor = Color.White.copy(alpha = 0.018f)
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color       = scanColor,
                            start       = Offset(0f, y),
                            end         = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += 4f
                    }
                }
        )

        // Diagonal gold highlight — upper-right corner bloom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors  = listOf(
                            VelocitaColors.GoldPrimary.copy(alpha = 0.07f),
                            Color.Transparent
                        ),
                        center  = Offset(Float.MAX_VALUE, 0f),
                        radius  = 900f
                    )
                )
        )

        // ── Content Column ────────────────────────────────────
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            // ── Gold monogram emblem ──────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                VelocitaColors.GoldPrimary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .drawBehind {
                        // Gold ring border
                        drawCircle(
                            color  = VelocitaColors.GoldPrimary.copy(alpha = 0.5f),
                            radius = size.minDimension / 2f,
                            style  = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = 1.5f
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "V",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = VelocitaColors.GoldPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Heading ───────────────────────────────────────
            Text(
                text  = "OWNER ACCESS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color        = VelocitaColors.SilverLight,
                    letterSpacing = 4.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text  = "Welcome back to your private portfolio",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = VelocitaColors.SilverMid
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── Form card — slightly elevated surface ─────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    // Apply horizontal shake offset on failed validation
                    .offset(x = shakeOffset.value.dp),
                color  = VelocitaColors.CharcoalDark.copy(alpha = 0.8f),
                shape  = RoundedCornerShape(8.dp),
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // ── Email field ───────────────────────────
                    // Real-time error: error clears the moment the user
                    // starts typing (handled in ViewModel.onLoginEmailChange)
                    VelocitaTextField(
                        value         = viewModel.loginEmail.value,
                        onValueChange = { value ->
                            viewModel.onLoginEmailChange(value)
                        },
                        label         = "EMAIL",
                        placeholder   = "your@email.com",
                        errorText     = viewModel.loginEmail.error,
                        leadingIcon   = {
                            Icon(
                                imageVector        = Icons.Default.Email,
                                contentDescription = null,
                                tint               = VelocitaColors.GoldPrimary
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction    = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // ── Password field ────────────────────────
                    VelocitaTextField(
                        value         = viewModel.loginPassword.value,
                        onValueChange = { value ->
                            viewModel.onLoginPasswordChange(value)
                        },
                        label         = "PASSWORD",
                        placeholder   = "Your secure password",
                        errorText     = viewModel.loginPassword.error,
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector        = Icons.Default.Lock,
                                contentDescription = null,
                                tint               = VelocitaColors.GoldPrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector        = if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint               = VelocitaColors.SilverMid
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction    = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Forgot password link (non-functional placeholder)
            TextButton(
                onClick  = { /* TODO: forgot password flow */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text  = "FORGOT PASSWORD?",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color        = VelocitaColors.GoldMuted,
                        letterSpacing = 1.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Sign In Button ────────────────────────────────
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (viewModel.validateLogin()) {
                        onLoggedIn()
                    } else {
                        // Trigger the shake animation on failure
                        kotlinx.coroutines.CoroutineScope(
                            kotlinx.coroutines.Dispatchers.Main
                        ).launch { triggerShake() }
                    }
                },
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
                    imageVector        = Icons.Default.Key,
                    contentDescription = null,
                    modifier           = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text  = "SIGN IN",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 3.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Register CTA ──────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text  = "New to Velocità?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.SilverMid
                    )
                )
                TextButton(onClick = onNavigateToEnrollment) {
                    Text(
                        text  = "REQUEST ACCESS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color        = VelocitaColors.GoldPrimary,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
