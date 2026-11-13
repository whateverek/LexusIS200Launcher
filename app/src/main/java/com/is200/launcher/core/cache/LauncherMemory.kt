package com.is200.launcher.core.cache

import androidx.compose.runtime.mutableStateListOf
import com.is200.launcher.model.LauncherApp

object LauncherMemory {

    val installedApps =
        mutableStateListOf<LauncherApp>()

    val pinnedApps =
        mutableStateListOf<LauncherApp>()

    var hasLoadedApps = false
}