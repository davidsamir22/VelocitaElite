package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
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

// ─────────────────────────────────────────────────────────────
//  SCREEN 2 — VIP ENROLLMENT
//
//  A "Request Access" registration form for new clients.
//  Fields: Full Name · Email · Password · Phone Number
//
//  Background: diagonal mesh gradient in warm charcoal +
//              a vertical gold accent strip on the left edge.
//
//  Validation is performed on the "Request Access" button press
//  and error states are displayed below each field.
// ─────────────────────────────────────────────────────────────

@Composable
fun EnrollmentScreen(
    viewModel            : MainViewModel,
    onEnrolled           : () -> Unit,
    onNavigateToLogin    : () -> Unit
) {
    val focusManager = LocalFocusManager.current

    // Track password visibility toggle
    var passwordVisible by remember { mutableStateOf(false) }

    // ── Root box with layered background ─────────────────────
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background: Diagonal gradient mesh ───────────────
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

        // Faint diagonal "carbon fibre" lines
        Canvas(modifier = Modifier.fillMaxSize()) {
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

        // ── Scrollable Content ────────────────────────────────
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(72.dp))

            // ── Header section ────────────────────────────────
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

            // ── Gold divider ──────────────────────────────────
            GoldDivider()

            Spacer(modifier = Modifier.height(36.dp))

            // ── Full Name Field ───────────────────────────────
            VelocitaTextField(
                value        = viewModel.enrollName.value,
                onValueChange = viewModel::onEnrollNameChange,
                label        = "FULL NAME",
                placeholder  = "e.g. Alessandro Ferrari",
                errorText    = viewModel.enrollName.error,
                leadingIcon  = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = VelocitaColors.GoldPrimary
                    )
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

            // ── Email Field ───────────────────────────────────
            VelocitaTextField(
                value         = viewModel.enrollEmail.value,
                onValueChange = viewModel::onEnrollEmailChange,
                label         = "EMAIL ADDRESS",
                placeholder   = "name@domain.com",
                errorText     = viewModel.enrollEmail.error,
                leadingIcon   = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = VelocitaColors.GoldPrimary
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

            Spacer(modifier = Modifier.height(20.dp))

            // ── Password Field ────────────────────────────────
            VelocitaTextField(
                value         = viewModel.enrollPassword.value,
                onValueChange = viewModel::onEnrollPasswordChange,
                label         = "PASSWORD",
                placeholder   = "Min 8 chars, include a letter & digit",
                errorText     = viewModel.enrollPassword.error,
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
                    // Toggle password visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector        = if (passwordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password"
                            else "Show password",
                            tint               = VelocitaColors.SilverMid
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

            // ── Phone Number Field ────────────────────────────
            VelocitaTextField(
                value         = viewModel.enrollPhone.value,
                onValueChange = viewModel::onEnrollPhoneChange,
                label         = "PHONE NUMBER",
                placeholder   = "+44 7700 900000",
                errorText     = viewModel.enrollPhone.error,
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Default.Phone,
                        contentDescription = null,
                        tint               = VelocitaColors.GoldPrimary
                    )
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

            // ── Submit Button ─────────────────────────────────
            Button(
                onClick = {
                    focusManager.clearFocus()
                    // Run validation; navigate only if all fields pass
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

            // ── Already a member link ─────────────────────────
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

// ─────────────────────────────────────────────────────────────
//  SHARED COMPOSABLES
//  Defined here; could be moved to a ui/components package
//  for a production codebase.
// ─────────────────────────────────────────────────────────────

/**
 * Branded gold gradient horizontal divider.
 */
@Composable
fun GoldDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.5f)
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
}

/**
 * A reusable branded text field styled to match the Luxury Dark theme.
 *
 * @param value                  Current text value.
 * @param onValueChange          Callback fired on every keystroke.
 * @param label                  Floating label text (ALL-CAPS convention).
 * @param placeholder            Hint text shown when the field is empty.
 * @param errorText              Optional error string; displayed below the field in red.
 *                               Pass null or empty string to hide the error.
 * @param visualTransformation   Pass [PasswordVisualTransformation] for masked input.
 * @param leadingIcon            Optional leading icon slot.
 * @param trailingIcon           Optional trailing icon slot (e.g. visibility toggle).
 * @param keyboardOptions        IME / keyboard type configuration.
 * @param keyboardActions        IME action callbacks (Next, Done, etc.).
 */
@Composable
fun VelocitaTextField(
    value                : String,
    onValueChange        : (String) -> Unit,
    label                : String,
    placeholder          : String,
    errorText            : String?            = null,
    visualTransformation : VisualTransformation = VisualTransformation.None,
    leadingIcon          : @Composable (() -> Unit)? = null,
    trailingIcon         : @Composable (() -> Unit)? = null,
    keyboardOptions      : KeyboardOptions    = KeyboardOptions.Default,
    keyboardActions      : KeyboardActions    = KeyboardActions.Default
) {
    val isError = !errorText.isNullOrBlank()

    Column(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            label                = {
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.2.sp
                    )
                )
            },
            placeholder          = {
                Text(
                    text  = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.SilverDim
                    )
                )
            },
            isError              = isError,
            visualTransformation = visualTransformation,
            leadingIcon          = leadingIcon,
            trailingIcon         = trailingIcon,
            keyboardOptions      = keyboardOptions,
            keyboardActions      = keyboardActions,
            singleLine           = true,
            shape                = RoundedCornerShape(4.dp),
            colors               = OutlinedTextFieldDefaults.colors(
                // Text colours
                focusedTextColor    = VelocitaColors.SilverLight,
                unfocusedTextColor  = VelocitaColors.SilverLight,
                errorTextColor      = VelocitaColors.SilverLight,
                // Label colours
                focusedLabelColor   = VelocitaColors.GoldPrimary,
                unfocusedLabelColor = VelocitaColors.SilverMid,
                errorLabelColor     = VelocitaColors.ErrorRed,
                // Border colours
                focusedBorderColor  = VelocitaColors.GoldPrimary,
                unfocusedBorderColor = VelocitaColors.CharcoalLight,
                errorBorderColor    = VelocitaColors.ErrorRed,
                // Container fill
                focusedContainerColor   = VelocitaColors.CharcoalMid.copy(alpha = 0.5f),
                unfocusedContainerColor = VelocitaColors.CharcoalMid.copy(alpha = 0.3f),
                errorContainerColor     = VelocitaColors.ErrorRed.copy(alpha = 0.05f),
                // Cursor
                cursorColor = VelocitaColors.GoldPrimary,
                errorCursorColor = VelocitaColors.ErrorRed
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // ── Inline error message ──────────────────────────────
        if (isError) {
            Row(
                modifier           = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 4.dp),
                verticalAlignment  = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector        = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint               = VelocitaColors.ErrorRed,
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text  = errorText!!,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VelocitaColors.ErrorRed
                    )
                )
            }
        }
    }
}

// Needed for Canvas usage in this file
@Composable
private fun Canvas(modifier: Modifier, onDraw: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit) {
    androidx.compose.foundation.Canvas(modifier = modifier, onDraw = onDraw)
}
