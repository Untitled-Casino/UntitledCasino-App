package com.example.untitledcasino.game

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import untitledcasino.composeapp.generated.resources.*

data class GameInfo(
    val title: StringResource,
    val resource: DrawableResource,
    val gameID: String,
)

val GAMES = listOf(
    GameInfo(
        title = Res.string.coin_flip_title,
        resource = Res.drawable.coin_flip_image,
        gameID = "coinflip",
    ),
    GameInfo(
        title = Res.string.hi_lo_title,
        resource = Res.drawable.hi_lo_image,
        gameID = "hilo",
    ),
    GameInfo(
        title = Res.string.daily_number_title,
        resource = Res.drawable.daily_number_image,
        gameID = "dailynumber",
    ),
)
