package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors
import com.velocita.elite.viewmodel.BikeModel
import com.velocita.elite.viewmodel.MainViewModel

// ─────────────────────────────────────────────────────────────
//  SCREEN 4 — THE SHOWROOM
//
//  Displays the full motorcycle catalogue as a scrollable list
//  of luxury cards.  Each card carries:
//    • Brand + model name
//    • Tagline / short description
//    • Key spec badges (HP · CC · Speed)
//    • Price
//    • "Configure" navigation button
//
//  Background: a horizontally tiled "metal mesh" pattern formed
//  by crossing diagonal lines on a deep charcoal base — distinct
//  from every other screen.
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowroomScreen(
    viewModel            : MainViewModel,
    onBikeSelected       : (Int) -> Unit,
    onConciergeRequested : () -> Unit,
    onAccountRequested   : () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background: metal mesh ────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0B0F))
                .drawBehind {
                    // Crossing diagonal lines at +45° and −45° create
                    // a subtle diamond mesh texture.
                    val meshColor = VelocitaColors.CharcoalLight.copy(alpha = 0.2f)
                    val step = 36f

                    // ↗ lines
                    var x = -size.height
                    while (x < size.width + size.height) {
                        drawLine(
                            color       = meshColor,
                            start       = Offset(x, 0f),
                            end         = Offset(x + size.height, size.height),
                            strokeWidth = 0.5f
                        )
                        x += step
                    }
                    // ↘ lines
                    x = -size.height
                    while (x < size.width + size.height) {
                        drawLine(
                            color       = meshColor,
                            start       = Offset(x + size.height, 0f),
                            end         = Offset(x, size.height),
                            strokeWidth = 0.5f
                        )
                        x += step
                    }

                    // Gold horizon line beneath the top bar
                    drawLine(
                        color       = VelocitaColors.GoldPrimary.copy(alpha = 0.3f),
                        start       = Offset(0f, 130f),
                        end         = Offset(size.width, 130f),
                        strokeWidth = 0.8f
                    )
                }
        )

        // ── Main Scaffold ─────────────────────────────────────
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ShowroomTopBar(
                    onAccountRequested   = onAccountRequested,
                    onConciergeRequested = onConciergeRequested
                )
            }
        ) { innerPadding ->

            LazyColumn(
                modifier            = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding      = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = 12.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Section heading
                item {
                    Text(
                        text  = "AVAILABLE MODELS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color        = VelocitaColors.SilverMid,
                            letterSpacing = 3.sp
                        ),
                        modifier = Modifier.padding(
                            start  = 4.dp,
                            top    = 8.dp,
                            bottom = 4.dp
                        )
                    )
                }

                // ── Bike cards ────────────────────────────────
                itemsIndexed(viewModel.bikeList) { index, bike ->
                    BikeCard(
                        bike     = bike,
                        onClick  = { onBikeSelected(bike.id) }
                    )
                }

                // Bottom CTA
                item {
                    ConciergeCallout(
                        onConciergeRequested = onConciergeRequested
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  TOP APP BAR
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowroomTopBar(
    onAccountRequested   : () -> Unit,
    onConciergeRequested : () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text  = "VELOCITÀ ELITE",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color        = VelocitaColors.GoldPrimary,
                        letterSpacing = 3.sp,
                        fontWeight   = FontWeight.Bold
                    )
                )
                Text(
                    text  = "THE SHOWROOM",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color        = VelocitaColors.SilverMid,
                        letterSpacing = 2.sp
                    )
                )
            }
        },
        actions = {
            // Concierge shortcut
            IconButton(onClick = onConciergeRequested) {
                Icon(
                    imageVector        = Icons.Default.HeadsetMic,
                    contentDescription = "Concierge",
                    tint               = VelocitaColors.SilverMid
                )
            }
            // Account / Login shortcut
            IconButton(onClick = onAccountRequested) {
                Icon(
                    imageVector        = Icons.Default.AccountCircle,
                    contentDescription = "Account",
                    tint               = VelocitaColors.SilverMid
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

// ─────────────────────────────────────────────────────────────
//  BIKE CARD
// ─────────────────────────────────────────────────────────────

/**
 * Renders a single luxury motorcycle card in the showroom list.
 *
 * @param bike    The [BikeModel] data to display.
 * @param onClick Called when the user taps anywhere on the card
 *                (or the Configure button specifically).
 */
@Composable
private fun BikeCard(
    bike    : BikeModel,
    onClick : () -> Unit
) {
    // Parse the bike's brand colour for a subtle tinted gradient
    val bikeColor = remember(bike.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(bike.colorHex))
        } catch (e: Exception) {
            VelocitaColors.GoldPrimary
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape          = RoundedCornerShape(8.dp),
        color          = VelocitaColors.CharcoalDark,
        tonalElevation = 2.dp
    ) {
        Box {
            // Left colour accent strip — tinted with bike brand colour
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                bikeColor.copy(alpha = 0.9f),
                                bikeColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )

            // Background tint from bike colour (very subtle)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                bikeColor.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Card body content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {

                // ── Brand ─────────────────────────────────────
                Text(
                    text  = bike.brand.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color        = bikeColor.copy(alpha = 0.9f),
                        letterSpacing = 2.sp
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // ── Model name ────────────────────────────────
                Text(
                    text  = bike.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = VelocitaColors.SilverLight
                    ),
                    maxLines  = 1,
                    overflow  = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // ── Tagline ───────────────────────────────────
                Text(
                    text  = bike.tagline,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.SilverMid
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Spec badges ───────────────────────────────
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpecBadge(label = "${bike.horsePower} HP")
                    SpecBadge(label = "${bike.engineCC} CC")
                    SpecBadge(label = "${bike.topSpeedKph} KPH")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Price + CTA row ───────────────────────────
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text  = "FROM",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color        = VelocitaColors.SilverDim,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text  = bike.priceDisplay,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color      = VelocitaColors.GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Configure button — navigates to Bike Config screen
                    OutlinedButton(
                        onClick = onClick,
                        shape   = RoundedCornerShape(4.dp),
                        border  = androidx.compose.foundation.BorderStroke(
                            1.dp, VelocitaColors.GoldPrimary
                        ),
                        colors  = ButtonDefaults.outlinedButtonColors(
                            contentColor = VelocitaColors.GoldPrimary
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp, vertical = 10.dp
                        )
                    ) {
                        Text(
                            text  = "CONFIGURE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                letterSpacing = 1.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector        = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier           = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  SPEC BADGE
// ─────────────────────────────────────────────────────────────

/**
 * A small pill badge used to display a single spec value.
 *
 * @param label The spec string to display (e.g. "240 HP").
 */
@Composable
private fun SpecBadge(label: String) {
    Box(
        modifier = Modifier
            .border(
                width  = 1.dp,
                color  = VelocitaColors.CharcoalLight,
                shape  = RoundedCornerShape(2.dp)
            )
            .background(
                color = VelocitaColors.CharcoalMid,
                shape = RoundedCornerShape(2.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelMedium.copy(
                color        = VelocitaColors.SilverMid,
                letterSpacing = 0.8.sp
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────
//  CONCIERGE CALLOUT BANNER
// ─────────────────────────────────────────────────────────────

/**
 * Full-width callout at the bottom of the list encouraging
 * the user to book a private viewing via the Concierge screen.
 */
@Composable
private fun ConciergeCallout(onConciergeRequested: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onConciergeRequested() },
        shape          = RoundedCornerShape(8.dp),
        color          = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            VelocitaColors.GoldMuted.copy(alpha = 0.15f),
                            VelocitaColors.GoldPrimary.copy(alpha = 0.08f)
                        )
                    )
                )
                .border(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(
                            VelocitaColors.GoldPrimary.copy(alpha = 0.5f),
                            VelocitaColors.GoldPrimary.copy(alpha = 0.1f)
                        )
                    ),
                    RoundedCornerShape(8.dp)
                )
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment      = Alignment.CenterVertically,
                horizontalArrangement  = Arrangement.SpaceBetween,
                modifier               = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text  = "PRIVATE CONCIERGE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color        = VelocitaColors.GoldPrimary,
                            letterSpacing = 2.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text  = "Book a private viewing or arrange bespoke delivery",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VelocitaColors.SilverMid
                        )
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector        = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint               = VelocitaColors.GoldPrimary,
                    modifier           = Modifier.size(28.dp)
                )
            }
        }
    }
}
