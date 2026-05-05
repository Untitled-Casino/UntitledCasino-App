package com.example.untitledcasino.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceBox(
    text: String,
    isDimmed: Boolean,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    baseContainerColor: Color = MaterialTheme.colorScheme.primary,
    baseTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(60.dp) // Fixed height to make them look like bricks
            .background(
                color = when {
                    !isEnabled || isDimmed -> baseContainerColor.copy(alpha = 0.5f)
                    else -> baseContainerColor
                },
                shape = RectangleShape // This makes it a sharp box
            )
            .clickable(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = when {
                !isEnabled || isDimmed -> baseTextColor.copy(alpha = 0.5f)
                else -> baseTextColor
            },
            fontWeight = FontWeight.Bold
        )
    }
}