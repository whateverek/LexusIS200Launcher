package com.is200.launcher.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object LauncherState {

    var currentScreen by mutableStateOf(
        LauncherScreen.HOME
    )
}