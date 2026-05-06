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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceBox(
    text: String,
    isDimmed: Boolean = false,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    baseContainerColor: Color = MaterialTheme.colorScheme.primary,
    baseTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .background(
                color = when {
                    !isEnabled || isDimmed -> baseContainerColor.copy(alpha = 0.5f)
                    else -> baseContainerColor
                },
                shape = RectangleShape,
            ).clickable(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = when {
                !isEnabled || isDimmed -> baseTextColor.copy(alpha = 0.5f)
                else -> baseTextColor
            },
            fontWeight = FontWeight.Bold,
            style = style,
        )
    }
}
