package com.is200.launcher.model

import android.graphics.drawable.Drawable

data class LauncherApp(

    val packageName: String,

    val appName: String,

    val icon: Drawable,

    val launchActivity: String
)