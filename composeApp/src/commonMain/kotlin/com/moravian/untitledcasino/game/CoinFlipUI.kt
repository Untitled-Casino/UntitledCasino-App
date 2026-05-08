package com.moravian.untitledcasino.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moravian.untitledcasino.game.vm.CoinFlipVM
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.*

@Composable
fun CoinFlipControls(vm: CoinFlipVM) {
    Column(
        modifier = Modifier.width(360.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ChoiceBox(
                text = stringResource(Res.string.heads),
                isDimmed = vm.sideSelection == "T",
                isEnabled = !vm.isBusy,
                modifier = Modifier.weight(1f),
                onClick = { vm.select("H") },
            )
            ChoiceBox(
                text = stringResource(Res.string.tails),
                isDimmed = vm.sideSelection == "H",
                isEnabled = !vm.isBusy,
                modifier = Modifier.weight(1f),
                onClick = { vm.select("T") },
            )
        }

        ChoiceBox(
            text = stringResource(Res.string.play),
            isDimmed = false,
            isEnabled = !vm.isBusy && vm.sideSelection.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            baseContainerColor = Color(0xFF0c9631),
            onClick = { vm.flip() },
        )
    }
}

@Composable
fun CoinFlipVisuals(vm: CoinFlipVM) {
    val rotation = remember { Animatable(0f) }
    var showResultOverlay by remember { mutableStateOf(false) }
    var hasWonState by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(vm.isBusy) {
        if (vm.isBusy) {
            showResultOverlay = false

            val targetSideAngle = if (vm.lastResult == "T") 180f else 0f

            // math so that rotation is consistent with VM result
            val baseRotation = (rotation.value / 360f).toInt() * 360f
            val totalTarget = baseRotation + 1080f + targetSideAngle

            rotation.animateTo(
                targetValue = totalTarget,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            )

            hasWonState = vm.won
            delay(300)
            showResultOverlay = true
            vm.onAnimationFinished()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(vm.uiMessage, color = Color.Red)

        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        rotationY = rotation.value
                        cameraDistance = 12f * density
                    }.background(Color(0xFFFFD700), CircleShape)
                    .border(4.dp, Color(0xFFB8860B), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                val normalizedRotation = (rotation.value % 360f + 360f) % 360f
                val isBackSide = normalizedRotation in 90f..270f

                Text(
                    text = if (isBackSide) "T" else "H",
                    modifier = Modifier.graphicsLayer {
                        if (isBackSide) rotationY = 180f
                    },
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                )
            }

            Column {
                AnimatedVisibility(
                    visible = showResultOverlay,
                    enter = fadeIn() + scaleIn(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (hasWonState == true) stringResource(Res.string.winner) else stringResource(Res.string.try_again),
                            color = if (hasWonState == true) Color.Green else Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                        )
                    }
                }
            }
        }
    }
}
