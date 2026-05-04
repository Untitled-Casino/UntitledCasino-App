package com.example.untitledcasino.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.untitledcasino.game.vm.HiLoVM
import kotlinx.coroutines.delay

@Composable
fun HiLoControls(vm: HiLoVM) {
    Column(
        modifier = Modifier.width(360.dp), // Match CoinFlip width
        verticalArrangement = Arrangement.spacedBy(8.dp) // Match CoinFlip spacing
    ) {
        // Top Row: Higher and Lower
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChoiceBox(
                text = "HIGHER",
                isDimmed = false,
                isEnabled = vm.isStreakActive && !vm.isBusy,
                modifier = Modifier.weight(1f),
                onClick = { vm.makeGuess(higher = true) }
            )
            ChoiceBox(
                text = "LOWER",
                isDimmed = false,
                isEnabled = vm.isStreakActive && !vm.isBusy,
                modifier = Modifier.weight(1f),
                onClick = { vm.makeGuess(higher = false) }
            )
        }

        // Bottom Row: Swappable Play/Cash Out button
        if (!vm.isStreakActive) {
            ChoiceBox(
                text = "PLAY",
                isDimmed = false,
                isEnabled = !vm.isBusy,
                modifier = Modifier.fillMaxWidth(),
                baseContainerColor = Color(0xFF0c9631), // Success Green from CoinFlip
                onClick = { vm.startHiLo() }
            )
        } else {
            ChoiceBox(
                text = "CASH OUT",
                isDimmed = false,
                isEnabled = !vm.isBusy && vm.round > 0,
                modifier = Modifier.fillMaxWidth(),
                baseContainerColor = Color(0xFFe67e22), // Orange for "Safety/Cash Out"
                onClick = { vm.cashOut() }
            )
        }
    }
}

@Composable
fun HiLoVisuals(vm: HiLoVM) {
    var showResultOverlay by remember { mutableStateOf(false) }

    LaunchedEffect(vm.nextCard) {
        if (vm.nextCard != null) {
            delay(500)
            showResultOverlay = true
            delay(1500)
            showResultOverlay = false
            vm.completeTurn()
        }
    }

    LaunchedEffect(vm.gameWon) {
        if (vm.gameWon) {
            showResultOverlay = true
            delay(4000)
            showResultOverlay = false
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(contentAlignment = Alignment.Center) {
            // Your card visuals will go here
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CardBox(value = vm.currentCard ?: 0, isFaceUp = vm.isStreakActive)
                CardBox(value = vm.nextCard ?: 0, isFaceUp = vm.nextCard != null)
            }

            // Win/Loss Overlay
            if (showResultOverlay) {
                Box(
                    modifier = Modifier
                        .matchParentSize() // Covers the exact area of the cards
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val message = when {
                        !vm.isStreakActive && vm.gameWon -> "CONGRATS YOU WIN!"
                        vm.roundWon -> "NICE JOB!"
                        else -> "YOU LOSE"
                    }

                    Text(
                        text = message,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        ),
                        color = if (vm.roundWon) Color.Green else Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


// Helper to turn 1..13 into A, 2..10, J, Q, K
fun getRank(value: Int): String = when (value) {
    1 -> "A"
    11 -> "J"
    12 -> "Q"
    13 -> "K"
    else -> value.toString()
}

// Determine if the card should be Red (Hearts/Diamonds) or Black
fun getCardColor(value: Int): Color = if (value % 2 == 0) Color.Red else Color.Black

@Composable
fun CardBox(value: Int, isFaceUp: Boolean) {
    val rank = getRank(value)
    val cardColor = getCardColor(value)

    Box(
        modifier = Modifier
            .size(width = 100.dp, height = 140.dp)
            .background(
                color = if (isFaceUp) Color.White else Color(0xFF2c3e50),
                shape = RoundedCornerShape(8.dp)
            )
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isFaceUp) {
            // Top-Left Rank
            Text(
                text = rank,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = cardColor,
                fontWeight = FontWeight.Bold
            )

            // Center Large Rank
            Text(
                text = rank,
                style = MaterialTheme.typography.displayMedium,
                color = cardColor,
                fontWeight = FontWeight.Bold
            )

            // Bottom-Right Rank (Inverted)
            Text(
                text = rank,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .graphicsLayer { rotationZ = 180f }, // Flip it upside down
                style = MaterialTheme.typography.titleMedium,
                color = cardColor,
                fontWeight = FontWeight.Bold
            )
        } else {
            // Card Back Pattern
            Text(
                text = "♣︎", // Or any pattern
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 40.sp
            )
        }
    }
}