package com.example.untitledcasino

import androidx.compose.ui.window.ComposeUIViewController
import com.example.untitledcasino.data.getRoomDatabase
import edu.moravian.survey.getDatabaseBuilder

fun MainViewController() = ComposeUIViewController {
    App(getRoomDatabase(getDatabaseBuilder()))
}
