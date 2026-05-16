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

    Box(modifier = Modifier.fillMaxSize().background(VelocitaColors.Obsidian)) {
        
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
            // Hero Image - Aprilia RSV4
            Box {
                AsyncImage(
                    model = "https://apexlearning.org.uk/wp-content/uploads/2021/01/Motorbike-Maintenance-Diploma.jpg",
                    contentDescription = "Aprilia RSV4 Concierge",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Overlay for better text readability
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, VelocitaColors.Obsidian)
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
                            Icon(
                                imageVector        = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint               = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }

            Column(
                modifier            = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text      = "YOUR DEDICATED\nCONCIERGE AWAITS",
                    style     = MaterialTheme.typography.headlineMedium.copy(
                        color        = Color.White,
                        letterSpacing = 2.sp,
                        textAlign    = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Service Type
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

                OutlinedTextField(
                    value         = viewModel.booking.name.value,
                    onValueChange = viewModel::onBookingNameChange,
                    label         = { Text("FULL NAME") },
                    modifier      = Modifier.fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.Person, contentDescription = null) },
                    colors        = textFieldColors(false)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value         = viewModel.booking.phone.value,
                    onValueChange = viewModel::onBookingPhoneChange,
                    label         = { Text("CONTACT NUMBER") },
                    modifier      = Modifier.fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.Phone, contentDescription = null) },
                    colors        = textFieldColors(false)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value         = viewModel.booking.preferredDate,
                    onValueChange = viewModel::onDateChange,
                    label         = { Text("PREFERRED DATE") },
                    modifier      = Modifier.fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    colors        = textFieldColors(false)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.submitBooking() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VelocitaColors.GoldPrimary,
                        contentColor   = VelocitaColors.Obsidian
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("SUBMIT REQUEST", style = MaterialTheme.typography.labelLarge)
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
        color = if (isSelected) VelocitaColors.GoldPrimary else VelocitaColors.CharcoalDark,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VelocitaColors.GoldPrimary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) VelocitaColors.Obsidian else VelocitaColors.GoldPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isSelected) VelocitaColors.Obsidian else VelocitaColors.SilverMid
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
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = VelocitaColors.GoldPrimary,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "REQUEST RECEIVED",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "We will contact you shortly.",
            style = MaterialTheme.typography.bodyLarge.copy(color = VelocitaColors.SilverMid),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VelocitaColors.GoldPrimary, contentColor = VelocitaColors.Obsidian),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("RETURN TO SHOWROOM")
        }
    }
}

@Composable
private fun textFieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = if (isError) VelocitaColors.ErrorRed else VelocitaColors.GoldPrimary,
    unfocusedBorderColor = if (isError) VelocitaColors.ErrorRed else VelocitaColors.CharcoalLight,
    focusedLabelColor = if (isError) VelocitaColors.ErrorRed else VelocitaColors.GoldPrimary,
    cursorColor = VelocitaColors.GoldPrimary
)
