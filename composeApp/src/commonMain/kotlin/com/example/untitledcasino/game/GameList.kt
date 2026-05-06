package com.example.untitledcasino.game

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.coin_flip_image
import untitledcasino.composeapp.generated.resources.coin_flip_title
import untitledcasino.composeapp.generated.resources.daily_number_image
import untitledcasino.composeapp.generated.resources.daily_number_title
import untitledcasino.composeapp.generated.resources.hi_lo_image
import untitledcasino.composeapp.generated.resources.hi_lo_title
import untitledcasino.composeapp.generated.resources.piggy_bank

data class GameInfo(
    val title: StringResource,
    val resource: DrawableResource,
    val gameID: String,
)

val GAMES = listOf(
    GameInfo(
        title = Res.string.coin_flip_title,
        resource = Res.drawable.coin_flip_image,
        gameID = "coinflip"
    ),
    GameInfo(
        title = Res.string.hi_lo_title,
        resource = Res.drawable.hi_lo_image,
        gameID = "hilo"
    ),
    GameInfo(
        title = Res.string.daily_number_title,
        resource = Res.drawable.daily_number_image,
        gameID = "dailynumber"
    )
)