package com.moravian.untitledcasino

import androidx.compose.ui.window.ComposeUIViewController
import com.moravian.untitledcasino.data.getRoomDatabase
import edu.moravian.survey.getDatabaseBuilder

fun MainViewController() = ComposeUIViewController {
    App(getRoomDatabase(getDatabaseBuilder()))
}
