package com.moravian.untitledcasino.game

import androidx.compose.runtime.Composable
import com.moravian.untitledcasino.game.vm.GameVM

data class GameContent(
    val title: String,
    val viewModel: GameVM,
    val visuals: @Composable (() -> Unit),
    val controls: @Composable (() -> Unit),
)
