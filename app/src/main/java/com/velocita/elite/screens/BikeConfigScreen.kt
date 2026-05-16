package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeConfigScreen(
    bikeId          : Int,
    viewModel       : MainViewModel,
    onBack          : () -> Unit,
    onBookConcierge : () -> Unit
) {
    val bike = viewModel.getBikeById(bikeId)

    if (bike == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Model not found", color = VelocitaColors.ErrorRed)
        }
        return
    }

    val bikeColor = remember(bike.colorHex) {
        try { Color(android.graphics.Color.parseColor(bike.colorHex)) }
        catch (e: Exception) { VelocitaColors.GoldPrimary }
    }

    Box(modifier = Modifier.fillMaxSize().background(VelocitaColors.Obsidian)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // Hero Image
            Box {
                AsyncImage(
                    model = bike.imageUrl,
                    contentDescription = bike.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(VelocitaColors.CharcoalMid),
                    contentScale = ContentScale.Crop
                )

                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.padding(8.dp).background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(50))
                        ) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = bike.brand.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(color = bikeColor, letterSpacing = 4.sp)
                )
                Text(
                    text = bike.name,
                    style = MaterialTheme.typography.displayMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatDial(modifier = Modifier.weight(1f), value = "${bike.horsePower}", unit = "HP", label = "Peak Power", color = bikeColor)
                    StatDial(modifier = Modifier.weight(1f), value = "${bike.engineCC}", unit = "CC", label = "Displacement", color = VelocitaColors.GoldPrimary)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "THE MACHINE",
                    style = MaterialTheme.typography.labelLarge.copy(color = VelocitaColors.SilverMid, letterSpacing = 3.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description Image
                AsyncImage(
                    model = bike.descriptionImageUrl,
                    contentDescription = "${bike.name} details",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, bikeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = bike.description,
                    style = MaterialTheme.typography.bodyLarge.copy(color = VelocitaColors.SilverMid, lineHeight = 28.sp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onBookConcierge,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VelocitaColors.GoldPrimary, contentColor = VelocitaColors.Obsidian)
                ) {
                    Text("BOOK PRIVATE VIEWING", style = MaterialTheme.typography.labelLarge)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun StatDial(value: String, unit: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = VelocitaColors.CharcoalDark
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = value, style = MaterialTheme.typography.headlineMedium.copy(color = color, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = unit, style = MaterialTheme.typography.labelSmall.copy(color = color.copy(alpha = 0.7f)))
            }
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = VelocitaColors.SilverDim))
        }
    }
}
