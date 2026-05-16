package com.velocita.elite.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.MainViewModel
import kotlin.math.sin

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

    val waveProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        waveProgress.animateTo(
            targetValue   = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation  = tween(2800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val swatchColors = listOf(
        bikeColor,
        Color(0xFF1A1A1A),
        Color(0xFFC0C0C0),
        Color(0xFFFFFFFF),
    )
    var selectedSwatch by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().background(VelocitaColors.Obsidian)) {
        // Animated Wave Background with Glow
        Canvas(
            modifier = Modifier.fillMaxWidth().height(180.dp).align(Alignment.TopCenter)
        ) {
            val w = size.width
            val midY = size.height * 0.6f
            val amp = size.height * 0.25f
            val path = Path()
            var first = true
            for (px in 0..w.toInt() step 2) {
                val t = px / w.toFloat()
                val y = midY + sin(t * 3 * Math.PI + waveProgress.value).toFloat() * amp
                if (first) { path.moveTo(px.toFloat(), y); first = false }
                else { path.lineTo(px.toFloat(), y) }
            }
            drawPath(path = path, color = bikeColor.copy(alpha = 0.35f), style = Stroke(width = 2f))
            
            val fillPath = Path().apply {
                addPath(path)
                lineTo(w, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(fillPath, Brush.verticalGradient(listOf(bikeColor.copy(alpha = 0.10f), Color.Transparent)))
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // Hero Image
            Box {
                AsyncImage(
                    model = bike.imageUrl,
                    contentDescription = bike.name,
                    modifier = Modifier.fillMaxWidth().height(300.dp).background(VelocitaColors.CharcoalMid),
                    contentScale = ContentScale.Crop
                )

                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = onBookConcierge) {
                            Icon(Icons.Default.HeadsetMic, contentDescription = "Concierge", tint = VelocitaColors.GoldPrimary)
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
                Text(
                    text = bike.priceDisplay,
                    style = MaterialTheme.typography.titleLarge.copy(color = VelocitaColors.GoldPrimary, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Performance Grid
                Text("PERFORMANCE", style = MaterialTheme.typography.labelLarge.copy(color = VelocitaColors.SilverMid, letterSpacing = 3.sp))
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatDial(modifier = Modifier.weight(1f), value = "${bike.horsePower}", unit = "HP", label = "Peak Power", color = bikeColor)
                    StatDial(modifier = Modifier.weight(1f), value = "${bike.engineCC}", unit = "CC", label = "Displacement", color = VelocitaColors.GoldPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatDial(modifier = Modifier.weight(1f), value = "${bike.topSpeedKph}", unit = "KPH", label = "Top Speed", color = VelocitaColors.SilverMid)
                    StatDial(modifier = Modifier.weight(1f), value = "${bike.weightKg}", unit = "KG", label = "Weight", color = VelocitaColors.SilverMid)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Livery Selector
                Text("LIVERY", style = MaterialTheme.typography.labelLarge.copy(color = VelocitaColors.SilverMid, letterSpacing = 3.sp))
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    swatchColors.forEachIndexed { index, color ->
                        ColorSwatch(color = color, isSelected = selectedSwatch == index, onClick = { selectedSwatch = index })
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("THE MACHINE", style = MaterialTheme.typography.labelLarge.copy(color = VelocitaColors.SilverMid, letterSpacing = 3.sp))
                Spacer(modifier = Modifier.height(16.dp))
                
                AsyncImage(
                    model = bike.descriptionImageUrl,
                    contentDescription = "${bike.name} details",
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)).border(1.dp, bikeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Left-bordered description block
                Row {
                    Box(modifier = Modifier.width(2.dp).height(IntrinsicSize.Max).background(Brush.verticalGradient(listOf(bikeColor.copy(alpha = 0.7f), bikeColor.copy(alpha = 0.1f)))))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = bike.description,
                        style = MaterialTheme.typography.bodyLarge.copy(color = VelocitaColors.SilverMid, lineHeight = 28.sp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onBookConcierge,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VelocitaColors.GoldPrimary, contentColor = VelocitaColors.Obsidian)
                ) {
                    Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("RESERVE THIS MODEL", style = MaterialTheme.typography.labelLarge)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedButton(
                    onClick = onBookConcierge,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VelocitaColors.CharcoalLight)
                ) {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("BOOK PRIVATE VIEWING", color = VelocitaColors.SilverMid)
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
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxSize().drawBehind { drawCircle(color.copy(alpha = 0.06f), size.minDimension * 0.7f, Offset(0f, 0f)) })
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
}

@Composable
private fun ColorSwatch(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(36.dp).border(width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) VelocitaColors.GoldPrimary else VelocitaColors.CharcoalLight, shape = RoundedCornerShape(50)).padding(4.dp).background(color = color, shape = RoundedCornerShape(50)).clickable(onClick = onClick)
    )
}
