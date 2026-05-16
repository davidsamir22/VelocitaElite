package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

// ─────────────────────────────────────────────────────────────
//  SCREEN 6 — CONCIERGE SERVICE
//
//  Allows clients to request:
//    • Scheduled maintenance
//    • Private showroom viewing
//
//  Form fields: Name · Phone · Service Type (toggle) · Date ·
//               Notes (optional)
//
//  On successful submission: shows a confirmation panel.
//
//  Background: "burnished gold foil" — a complex multi-stop
//  radial gradient with warm amber highlights and dark charcoal,
//  completely distinct from every other screen.
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConciergeScreen(
    viewModel : MainViewModel,
    onBack    : () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background: Burnished gold foil effect ────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colorStops = arrayOf(
                            0.00f to Color(0xFF0D0B08),
                            0.30f to Color(0xFF12100A),
                            0.60f to Color(0xFF100E0A),
                            0.85f to Color(0xFF0C0B09),
                            1.00f to Color(0xFF090808)
                        )
                    )
                )
                // Draw the "foil shimmer" overlay via drawBehind
                .drawBehind {
                    // Warm amber radial bloom — upper left
                    drawCircle(
                        brush  = Brush.radialGradient(
                            listOf(
                                Color(0xFFD4A843).copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.15f, size.height * 0.1f),
                            radius = size.width * 0.5f
                        ),
                        radius = size.width * 0.5f,
                        center = Offset(size.width * 0.15f, size.height * 0.1f)
                    )

                    // Cool dark bloom — lower right (counterbalances warmth)
                    drawCircle(
                        brush  = Brush.radialGradient(
                            listOf(
                                Color(0xFF1A1530).copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.9f, size.height * 0.85f),
                            radius = size.width * 0.6f
                        ),
                        radius = size.width * 0.6f,
                        center = Offset(size.width * 0.9f, size.height * 0.85f)
                    )

                    // Fine horizontal hairlines — "brushed gold" texture
                    val lineColor = Color(0xFFD4A843).copy(alpha = 0.03f)
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color       = lineColor,
                            start       = Offset(0f, y),
                            end         = Offset(size.width, y),
                            strokeWidth = 0.5f
                        )
                        y += 3f
                    }
                }
        )

        // ── If booking already submitted: show success state ──
        if (viewModel.booking.submitted) {
            BookingConfirmation(
                onDismiss = {
                    viewModel.resetBooking()
                    onBack()
                }
            )
            return@Box
        }

        // ── Booking form ──────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Top bar
            TopAppBar(
                title = {
                    Text(
                        text  = "CONCIERGE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color        = VelocitaColors.GoldPrimary,
                            letterSpacing = 3.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint               = VelocitaColors.SilverLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier            = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // ── Screen header ─────────────────────────────
                Text(
                    text      = "YOUR DEDICATED\nCONCIERGE AWAITS",
                    style     = MaterialTheme.typography.headlineLarge.copy(
                        color        = VelocitaColors.SilverLight,
                        letterSpacing = 2.sp,
                        lineHeight   = 40.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text      = "Available 24 hours, 7 days a week",
                    style     = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.GoldMuted
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                GoldDivider()

                Spacer(modifier = Modifier.height(32.dp))

                // ── Service Type Toggle ───────────────────────
                Text(
                    text     = "SERVICE TYPE",
                    style    = MaterialTheme.typography.labelLarge.copy(
                        color        = VelocitaColors.SilverMid,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ServiceTypeChip(
                        label      = "Maintenance",
                        icon       = Icons.Default.Build,
                        isSelected = viewModel.booking.serviceType == "Maintenance",
                        onClick    = { viewModel.onServiceTypeChange("Maintenance") },
                        modifier   = Modifier.weight(1f)
                    )
                    ServiceTypeChip(
                        label      = "Private Viewing",
                        icon       = Icons.Default.Visibility,
                        isSelected = viewModel.booking.serviceType == "Private Viewing",
                        onClick    = { viewModel.onServiceTypeChange("Private Viewing") },
                        modifier   = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Name Field ────────────────────────────────
                VelocitaTextField(
                    value         = viewModel.booking.name.value,
                    onValueChange = viewModel::onBookingNameChange,
                    label         = "FULL NAME",
                    placeholder   = "Your registered name",
                    errorText     = viewModel.booking.name.error,
                    leadingIcon   = {
                        Icon(
                            imageVector        = Icons.Default.Person,
                            contentDescription = null,
                            tint               = VelocitaColors.GoldPrimary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction  = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Phone Field ───────────────────────────────
                VelocitaTextField(
                    value         = viewModel.booking.phone.value,
                    onValueChange = viewModel::onBookingPhoneChange,
                    label         = "CONTACT NUMBER",
                    placeholder   = "+44 7700 900000",
                    errorText     = viewModel.booking.phone.error,
                    leadingIcon   = {
                        Icon(
                            imageVector        = Icons.Default.Phone,
                            contentDescription = null,
                            tint               = VelocitaColors.GoldPrimary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction    = ImeAction.Next,
                        keyboardType = KeyboardType.Phone
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Preferred Date Field ──────────────────────
                // In production this would open a DatePickerDialog;
                // here a plain text field is used as a stand-in.
                VelocitaTextField(
                    value         = viewModel.booking.preferredDate,
                    onValueChange = viewModel::onDateChange,
                    label         = "PREFERRED DATE",
                    placeholder   = "DD / MM / YYYY",
                    leadingIcon   = {
                        Icon(
                            imageVector        = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint               = VelocitaColors.GoldPrimary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Notes Field (multi-line) ──────────────────
                OutlinedTextField(
                    value         = viewModel.booking.notes.value,
                    onValueChange = viewModel::onBookingNotesChange,
                    label         = {
                        Text(
                            text  = "ADDITIONAL NOTES",
                            style = MaterialTheme.typography.labelMedium.copy(
                                letterSpacing = 1.2.sp
                            )
                        )
                    },
                    placeholder   = {
                        Text(
                            text  = "Specific requirements, preferred test-ride routes…",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = VelocitaColors.SilverDim
                            )
                        )
                    },
                    minLines      = 4,
                    maxLines      = 6,
                    shape         = RoundedCornerShape(4.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor      = VelocitaColors.SilverLight,
                        unfocusedTextColor    = VelocitaColors.SilverLight,
                        focusedBorderColor    = VelocitaColors.GoldPrimary,
                        unfocusedBorderColor  = VelocitaColors.CharcoalLight,
                        focusedContainerColor = VelocitaColors.CharcoalMid.copy(alpha = 0.5f),
                        unfocusedContainerColor = VelocitaColors.CharcoalMid.copy(alpha = 0.3f),
                        cursorColor           = VelocitaColors.GoldPrimary,
                        focusedLabelColor     = VelocitaColors.GoldPrimary,
                        unfocusedLabelColor   = VelocitaColors.SilverMid
                    ),
                    modifier      = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(36.dp))

                // ── Submit button ─────────────────────────────
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.submitBooking()
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
                        imageVector        = Icons.Default.Send,
                        contentDescription = null,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text  = "SUBMIT REQUEST",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 2.sp
                        )
                    )
                }

                // Availability note
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text      = "A member of our team will contact you within 2 hours",
                    style     = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.SilverDim
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  SERVICE TYPE CHIP
// ─────────────────────────────────────────────────────────────

/**
 * A selectable service type pill.
 *
 * @param label      Display label.
 * @param icon       Icon shown to the left of the label.
 * @param isSelected Whether this chip is currently active.
 * @param onClick    Selection callback.
 */
@Composable
private fun ServiceTypeChip(
    label      : String,
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    isSelected : Boolean,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    val bgColor     = if (isSelected) VelocitaColors.GoldPrimary.copy(alpha = 0.12f)
                      else            VelocitaColors.CharcoalDark
    val borderColor = if (isSelected) VelocitaColors.GoldPrimary
                      else            VelocitaColors.CharcoalLight
    val textColor   = if (isSelected) VelocitaColors.GoldPrimary
                      else            VelocitaColors.SilverMid

    Box(
        modifier = modifier
            .border(
                width  = if (isSelected) 1.5.dp else 1.dp,
                color  = borderColor,
                shape  = RoundedCornerShape(4.dp)
            )
            .background(bgColor, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = textColor,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text  = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color        = textColor,
                    letterSpacing = 0.8.sp
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  BOOKING CONFIRMATION PANEL
// ─────────────────────────────────────────────────────────────

/**
 * Full-screen confirmation shown after a successful booking
 * submission.  Provides a prominent success icon, summary
 * message, and a dismiss / back button.
 *
 * @param onDismiss Called when the user taps "Return to Showroom".
 */
@Composable
private fun BookingConfirmation(onDismiss: () -> Unit) {
    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(
                        VelocitaColors.GoldPrimary.copy(alpha = 0.08f),
                        Color(0xFF0A0A0C)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large check icon in a gold ring
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        2.dp,
                        VelocitaColors.GoldPrimary,
                        RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint               = VelocitaColors.GoldPrimary,
                    modifier           = Modifier.size(56.dp)
                )
            }

            Text(
                text      = "REQUEST RECEIVED",
                style     = MaterialTheme.typography.headlineMedium.copy(
                    color        = VelocitaColors.SilverLight,
                    letterSpacing = 3.sp,
                    fontWeight   = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text      = "Your concierge will be in touch within two hours to " +
                            "confirm your appointment and discuss any bespoke requirements.",
                style     = MaterialTheme.typography.bodyLarge.copy(
                    color      = VelocitaColors.SilverMid,
                    lineHeight = 26.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick  = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = VelocitaColors.GoldPrimary,
                    contentColor   = VelocitaColors.Obsidian
                ),
                shape    = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text  = "RETURN TO SHOWROOM",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}
