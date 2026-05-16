package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConciergeScreen(
    viewModel : MainViewModel,
    onBack    : () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier
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
        .drawBehind {
            // "Foil shimmer" overlay from root version
            drawCircle(
                brush  = Brush.radialGradient(
                    listOf(Color(0xFFD4A843).copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(size.width * 0.15f, size.height * 0.1f),
                    radius = size.width * 0.5f
                ),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.15f, size.height * 0.1f)
            )
            drawCircle(
                brush  = Brush.radialGradient(
                    listOf(Color(0xFF1A1530).copy(alpha = 0.35f), Color.Transparent),
                    center = Offset(size.width * 0.9f, size.height * 0.85f),
                    radius = size.width * 0.6f
                ),
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.9f, size.height * 0.85f)
            )
            val lineColor = Color(0xFFD4A843).copy(alpha = 0.03f)
            var y = 0f
            while (y < size.height) {
                drawLine(lineColor, Offset(0f, y), Offset(size.width, y), 0.5f)
                y += 3f
            }
        }
    ) {
        
        if (viewModel.booking.submitted) {
            BookingConfirmation(
                onDismiss = {
                    viewModel.resetBooking()
                    onBack()
                }
            )
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image section
            Box {
                AsyncImage(
                    model = "https://apexlearning.org.uk/wp-content/uploads/2021/01/Motorbike-Maintenance-Diploma.jpg",
                    contentDescription = "Concierge Service",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Crop
                )
                
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color(0xFF0D0B08))
                            )
                        )
                )

                TopAppBar(
                    title = {
                        Text(
                            text  = "CONCIERGE",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 4.sp, color = Color.White)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(50))
                        ) {
                            Icon(Icons.Default.ArrowBackIosNew, "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }

            Column(
                modifier            = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text      = "YOUR DEDICATED\nCONCIERGE AWAITS",
                    style     = MaterialTheme.typography.headlineLarge.copy(
                        color        = Color.White,
                        letterSpacing = 2.sp,
                        textAlign    = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text      = "Available 24 hours, 7 days a week",
                    style     = MaterialTheme.typography.bodyMedium.copy(color = VelocitaColors.GoldMuted),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                GoldDivider()

                Spacer(modifier = Modifier.height(32.dp))

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

                VelocitaTextField(
                    value         = viewModel.booking.name.value,
                    onValueChange = viewModel::onBookingNameChange,
                    label         = "FULL NAME",
                    placeholder   = "Your registered name",
                    errorText     = viewModel.booking.name.error,
                    leadingIcon   = { Icon(Icons.Default.Person, null, tint = VelocitaColors.GoldPrimary) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                VelocitaTextField(
                    value         = viewModel.booking.phone.value,
                    onValueChange = viewModel::onBookingPhoneChange,
                    label         = "CONTACT NUMBER",
                    placeholder   = "+44 7700 900000",
                    errorText     = viewModel.booking.phone.error,
                    leadingIcon   = { Icon(Icons.Default.Phone, null, tint = VelocitaColors.GoldPrimary) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                VelocitaTextField(
                    value         = viewModel.booking.preferredDate,
                    onValueChange = viewModel::onDateChange,
                    label         = "PREFERRED DATE",
                    placeholder   = "DD / MM / YYYY",
                    leadingIcon   = { Icon(Icons.Default.CalendarMonth, null, tint = VelocitaColors.GoldPrimary) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.submitBooking() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VelocitaColors.GoldPrimary, contentColor = VelocitaColors.Obsidian),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Send, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("SUBMIT REQUEST", style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp))
                }

                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Composable
private fun ServiceTypeChip(
    label      : String,
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    isSelected : Boolean,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = if (isSelected) VelocitaColors.GoldPrimary.copy(alpha = 0.12f) else VelocitaColors.CharcoalDark,
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VelocitaColors.GoldPrimary else VelocitaColors.CharcoalLight)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) VelocitaColors.GoldPrimary else VelocitaColors.SilverMid,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (isSelected) VelocitaColors.GoldPrimary else VelocitaColors.SilverMid
                )
            )
        }
    }
}

@Composable
private fun BookingConfirmation(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).border(2.dp, VelocitaColors.GoldPrimary, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, null, tint = VelocitaColors.GoldPrimary, modifier = Modifier.size(56.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "REQUEST RECEIVED", style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, letterSpacing = 3.sp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your concierge will be in touch shortly to confirm your appointment.",
            style = MaterialTheme.typography.bodyLarge.copy(color = VelocitaColors.SilverMid),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VelocitaColors.GoldPrimary, contentColor = VelocitaColors.Obsidian),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("RETURN TO SHOWROOM", style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp))
        }
    }
}
