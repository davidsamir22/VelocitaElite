package com.velocita.elite.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onLoggedIn: () -> Unit,
    onNavigateToEnrollment: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun triggerShake() {
        val shakeSpec = tween<Float>(durationMillis = 50, easing = LinearEasing)
        repeat(4) {
            shakeOffset.animateTo(10f, shakeSpec)
            shakeOffset.animateTo(-10f, shakeSpec)
        }
        shakeOffset.animateTo(0f, shakeSpec)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background: vertical scan-line gradient
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val scanColor = Color.White.copy(alpha = 0.018f)
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = scanColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += 4f
                    }
                }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            VelocitaColors.GoldPrimary.copy(alpha = 0.07f),
                            Color.Transparent
                        ),
                        center = Offset(Float.MAX_VALUE, 0f),
                        radius = 900f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(64.dp))

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
                        drawCircle(
                            color = VelocitaColors.GoldPrimary.copy(alpha = 0.5f),
                            radius = size.minDimension / 2f,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "V",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = VelocitaColors.GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "OWNER ACCESS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = VelocitaColors.SilverLight,
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome back to your private portfolio",
                style = MaterialTheme.typography.bodyMedium.copy(color = VelocitaColors.SilverMid),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = shakeOffset.value.dp),
                color = VelocitaColors.CharcoalDark.copy(alpha = 0.8f),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    VelocitaTextField(
                        value = viewModel.loginEmail.value,
                        onValueChange = { viewModel.loginEmail = viewModel.loginEmail.copy(value = it, error = null) },
                        label = "EMAIL",
                        placeholder = "your@email.com",
                        errorText = viewModel.loginEmail.error,
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = VelocitaColors.GoldPrimary) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    VelocitaTextField(
                        value = viewModel.loginPassword.value,
                        onValueChange = { viewModel.loginPassword = viewModel.loginPassword.copy(value = it, error = null) },
                        label = "PASSWORD",
                        placeholder = "Your secure password",
                        errorText = viewModel.loginPassword.error,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = VelocitaColors.GoldPrimary) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = VelocitaColors.SilverMid
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (viewModel.validateLogin()) {
                        viewModel.setUserProfile("Elite Owner", viewModel.loginEmail.value)
                        onLoggedIn()
                    } else {
                        coroutineScope.launch { triggerShake() }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VelocitaColors.GoldPrimary, contentColor = VelocitaColors.Obsidian),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(Icons.Default.Key, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("SIGN IN", style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 3.sp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("New to Velocità?", style = MaterialTheme.typography.bodyMedium.copy(color = VelocitaColors.SilverMid))
                TextButton(onClick = onNavigateToEnrollment) {
                    Text("REQUEST ACCESS", style = MaterialTheme.typography.labelMedium.copy(color = VelocitaColors.GoldPrimary, letterSpacing = 1.sp))
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
