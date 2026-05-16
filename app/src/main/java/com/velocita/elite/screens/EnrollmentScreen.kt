package com.velocita.elite.screens

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel

@Composable
fun EnrollmentScreen(
    viewModel            : MainViewModel,
    onEnrolled           : () -> Unit,
    onNavigateToLogin    : () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background: Diagonal gradient mesh
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF0C0C10),
                            0.4f to Color(0xFF131318),
                            0.7f to Color(0xFF0E0E12),
                            1.0f to Color(0xFF0A0A0C)
                        )
                    )
                )
        )

        // Gold left-edge accent strip
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            VelocitaColors.GoldPrimary.copy(alpha = 0.7f),
                            VelocitaColors.GoldPrimary.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Faint diagonal lines
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val lineColor = VelocitaColors.CharcoalLight.copy(alpha = 0.25f)
            var x = -size.height.toInt()
            while (x < size.width.toInt() * 2) {
                drawLine(
                    color       = lineColor,
                    start       = androidx.compose.ui.geometry.Offset(x.toFloat(), 0f),
                    end         = androidx.compose.ui.geometry.Offset(
                        x + size.height, size.height
                    ),
                    strokeWidth = 0.8f
                )
                x += 28
            }
        }

        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(72.dp))

            Text(
                text  = "REQUEST",
                style = MaterialTheme.typography.displayMedium.copy(
                    color        = VelocitaColors.GoldPrimary,
                    letterSpacing = 6.sp
                )
            )
            Text(
                text  = "ACCESS",
                style = MaterialTheme.typography.displayMedium.copy(
                    color        = VelocitaColors.SilverLight,
                    letterSpacing = 6.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text      = "Join our exclusive clientele — private viewings\nand bespoke configuration await.",
                style     = MaterialTheme.typography.bodyMedium.copy(
                    color = VelocitaColors.SilverMid
                ),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            GoldDivider()

            Spacer(modifier = Modifier.height(36.dp))

            VelocitaTextField(
                value        = viewModel.enrollName.value,
                onValueChange = { viewModel.enrollName = viewModel.enrollName.copy(value = it, error = null) },
                label        = "FULL NAME",
                placeholder  = "e.g. Alessandro Ferrari",
                errorText    = viewModel.enrollName.error,
                leadingIcon  = {
                    Icon(Icons.Default.Person, null, tint = VelocitaColors.GoldPrimary)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            VelocitaTextField(
                value         = viewModel.enrollEmail.value,
                onValueChange = { viewModel.enrollEmail = viewModel.enrollEmail.copy(value = it, error = null) },
                label         = "EMAIL ADDRESS",
                placeholder   = "name@domain.com",
                errorText     = viewModel.enrollEmail.error,
                leadingIcon   = {
                    Icon(Icons.Default.Email, null, tint = VelocitaColors.GoldPrimary)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction    = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            VelocitaTextField(
                value         = viewModel.enrollPassword.value,
                onValueChange = { viewModel.enrollPassword = viewModel.enrollPassword.copy(value = it, error = null) },
                label         = "PASSWORD",
                placeholder   = "Min 8 chars, include a letter & digit",
                errorText     = viewModel.enrollPassword.error,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, null, tint = VelocitaColors.GoldPrimary)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = VelocitaColors.SilverMid
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction    = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            VelocitaTextField(
                value         = viewModel.enrollPhone.value,
                onValueChange = { viewModel.enrollPhone = viewModel.enrollPhone.copy(value = it, error = null) },
                label         = "PHONE NUMBER",
                placeholder   = "+44 7700 900000",
                errorText     = viewModel.enrollPhone.error,
                leadingIcon   = {
                    Icon(Icons.Default.Phone, null, tint = VelocitaColors.GoldPrimary)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction    = ImeAction.Done,
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (viewModel.validateEnrollment()) {
                        onEnrolled()
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
                Text(
                    text  = "REQUEST ACCESS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 3.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text  = "EXISTING OWNER?  SIGN IN",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color        = VelocitaColors.SilverMid,
                        letterSpacing = 1.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
