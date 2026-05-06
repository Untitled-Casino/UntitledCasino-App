package com.example.untitledcasino.game

import androidx.compose.runtime.Composable
import com.example.untitledcasino.game.vm.GameVM

data class GameContent(
    val title: String,
    val viewModel: GameVM,
    val visuals: @Composable (() -> Unit),
    val controls: @Composable (() -> Unit),
)
