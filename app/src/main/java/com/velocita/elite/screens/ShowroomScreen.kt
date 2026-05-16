package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
    Box(modifier = Modifier.fillMaxSize()) {
        // Background: metal mesh pattern from root version
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0B0F))
                .drawBehind {
                    val meshColor = VelocitaColors.CharcoalLight.copy(alpha = 0.2f)
                    val step = 36f
                    var x = -size.height
                    while (x < size.width + size.height) {
                        drawLine(meshColor, Offset(x, 0f), Offset(x + size.height, size.height), 0.5f)
                        x += step
                    }
                    x = -size.height
                    while (x < size.width + size.height) {
                        drawLine(meshColor, Offset(x + size.height, 0f), Offset(x, size.height), 0.5f)
                        x += step
                    }
                }
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("VELOCITÀ ELITE", style = MaterialTheme.typography.titleLarge.copy(color = VelocitaColors.GoldPrimary, letterSpacing = 3.sp, fontWeight = FontWeight.Bold))
                            Text("THE SHOWROOM", style = MaterialTheme.typography.labelMedium.copy(color = VelocitaColors.SilverMid, letterSpacing = 2.sp))
                        }
                    },
                    actions = {
                        IconButton(onClick = onConciergeRequested) {
                            Icon(Icons.Default.HeadsetMic, contentDescription = "Concierge", tint = VelocitaColors.SilverMid)
                        }
                        IconButton(onClick = onAccountRequested) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Account", tint = VelocitaColors.SilverMid)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.bikeList) { bike ->
                    BikeCard(bike = bike, onClick = { onBikeSelected(bike.id) })
                }
                
                item {
                    ConciergeCallout(onConciergeRequested = onConciergeRequested)
                }
            }
        }
    }
}

@Composable
private fun BikeCard(bike: BikeModel, onClick: () -> Unit) {
    val bikeColor = remember(bike.colorHex) {
        try { Color(android.graphics.Color.parseColor(bike.colorHex)) }
        catch (e: Exception) { VelocitaColors.GoldPrimary }
    }

    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = VelocitaColors.CharcoalDark,
        tonalElevation = 2.dp
    ) {
        Box {
            // Left color accent strip
            Box(
                modifier = Modifier.width(4.dp).matchParentSize().background(
                    Brush.verticalGradient(listOf(bikeColor.copy(alpha = 0.9f), bikeColor.copy(alpha = 0.3f)))
                )
            )

            Column {
                AsyncImage(
                    model = bike.imageUrl,
                    contentDescription = bike.name,
                    modifier = Modifier.fillMaxWidth().height(200.dp).background(VelocitaColors.CharcoalMid),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
                    Text(text = bike.brand.uppercase(), style = MaterialTheme.typography.labelMedium.copy(color = bikeColor.copy(alpha = 0.9f), letterSpacing = 2.sp))
                    Text(text = bike.name, style = MaterialTheme.typography.headlineMedium.copy(color = VelocitaColors.SilverLight))
                    Text(text = bike.tagline, style = MaterialTheme.typography.bodyMedium.copy(color = VelocitaColors.SilverMid), maxLines = 2, overflow = TextOverflow.Ellipsis)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SpecBadge(label = "${bike.horsePower} HP")
                        SpecBadge(label = "${bike.engineCC} CC")
                        SpecBadge(label = "${bike.topSpeedKph} KPH")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = bike.priceDisplay, style = MaterialTheme.typography.titleLarge.copy(color = VelocitaColors.GoldPrimary, fontWeight = FontWeight.Bold))
                        OutlinedButton(
                            onClick = onClick,
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, VelocitaColors.GoldPrimary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VelocitaColors.GoldPrimary)
                        ) {
                            Text("CONFIGURE", style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecBadge(label: String) {
    Surface(color = VelocitaColors.CharcoalMid, shape = RoundedCornerShape(2.dp), border = androidx.compose.foundation.BorderStroke(1.dp, VelocitaColors.CharcoalLight)) {
        Text(text = label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall.copy(color = VelocitaColors.SilverMid))
    }
}

@Composable
private fun ConciergeCallout(onConciergeRequested: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clickable { onConciergeRequested() },
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(VelocitaColors.GoldMuted.copy(alpha = 0.15f), VelocitaColors.GoldPrimary.copy(alpha = 0.08f))))
                .border(1.dp, VelocitaColors.GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("PRIVATE CONCIERGE", style = MaterialTheme.typography.labelLarge.copy(color = VelocitaColors.GoldPrimary, letterSpacing = 2.sp))
                    Text("Book a private viewing or arrange bespoke delivery", style = MaterialTheme.typography.bodyMedium.copy(color = VelocitaColors.SilverMid))
                }
                Icon(Icons.Default.ChevronRight, null, tint = VelocitaColors.GoldPrimary, modifier = Modifier.size(28.dp))
            }
        }
    }
}
