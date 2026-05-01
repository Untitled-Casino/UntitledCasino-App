package com.example.untitledcasino.game

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

data class GameContent (
    val title: String,
    val viewModel: ViewModel?,
    val visuals: @Composable (() -> Unit),
    val controls: @Composable (() -> Unit),
    )