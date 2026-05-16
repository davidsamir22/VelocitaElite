package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.BikeModel
import com.velocita.elite.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowroomScreen(
    viewModel: MainViewModel,
    onBikeSelected: (Int) -> Unit,
    onConciergeRequested: () -> Unit,
    onAccountRequested: () -> Unit
) {
    Scaffold(
        containerColor = VelocitaColors.Obsidian,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "THE SHOWROOM",
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 4.sp)
                    )
                },
                actions = {
                    IconButton(onClick = onConciergeRequested) {
                        Icon(Icons.Default.HeadsetMic, contentDescription = "Concierge", tint = VelocitaColors.GoldPrimary)
                    }
                    IconButton(onClick = onAccountRequested) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account", tint = VelocitaColors.GoldPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(viewModel.bikeList) { bike ->
                BikeCard(bike = bike, onClick = { onBikeSelected(bike.id) })
            }
        }
    }
}

@Composable
private fun BikeCard(bike: BikeModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, VelocitaColors.GoldPrimary, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = VelocitaColors.CharcoalDark)
    ) {
        Column {
            // Bike Image using Coil
            AsyncImage(
                model = bike.imageUrl,
                contentDescription = bike.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(VelocitaColors.CharcoalMid),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = bike.brand.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = VelocitaColors.GoldPrimary,
                                letterSpacing = 2.sp
                            )
                        )
                        Text(
                            text = bike.name,
                            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                        )
                    }
                    Surface(
                        color = VelocitaColors.GoldPrimary,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = bike.priceDisplay,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = VelocitaColors.Obsidian,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpecBadge(label = "${bike.horsePower} HP")
                    SpecBadge(label = "${bike.engineCC} CC")
                    SpecBadge(label = "${bike.topSpeedKph} KPH")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = bike.tagline,
                    style = MaterialTheme.typography.bodyMedium.copy(color = VelocitaColors.SilverMid),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VelocitaColors.GoldPrimary)
                ) {
                    Text("CONFIGURE", color = VelocitaColors.GoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = VelocitaColors.GoldPrimary)
                }
            }
        }
    }
}

@Composable
private fun SpecBadge(label: String) {
    Surface(
        color = VelocitaColors.CharcoalMid,
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VelocitaColors.CharcoalLight)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(color = VelocitaColors.SilverMid)
        )
    }
}
