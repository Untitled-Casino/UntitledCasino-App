package com.example.untitledcasino.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CasinoColorScheme = darkColorScheme( // darker color scheme should use this so android modifies system stuff
    primary = CasinoRed,
    onPrimary = CasinoWhite,
    secondary = CasinoDarkRed,
    background = CasinoBlack,
    onBackground = CasinoWhite,
    surface = CasinoGrey,
    onSurface = CasinoWhite,
)

@Composable
fun UntitledCasinoTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = CasinoColorScheme,
        content = content,
    )
}