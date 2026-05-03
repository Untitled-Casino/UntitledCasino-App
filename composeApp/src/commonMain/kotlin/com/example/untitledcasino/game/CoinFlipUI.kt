package com.example.untitledcasino.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.untitledcasino.game.vm.CoinFlipVM

@Composable
fun CoinFlipControls(vm: CoinFlipVM) {
    Column {
        Row() {
            Button(onClick = { vm.select("H") }) {
                Text("Heads")
            }
            Button(onClick = { vm.select("T") }) {
                Text("Tails")
            }
        }
        Button(onClick = { vm.flip() }) {
            Text("PLAY")
        }
    }

}

@Composable
fun CoinFlipVisuals(vm: CoinFlipVM) {
    Column() {
        Text(vm.uiMessage)
        Box() {
            Text(text = when(vm.won) {
                true -> "WINNER"
                false -> "LOSER"
                null -> "PLAY PLAY PLAY"
            })
        }

    }
}