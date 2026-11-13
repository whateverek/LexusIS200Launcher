package com.is200.launcher.ui.components.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun LauncherBackground() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(

                Brush.linearGradient(

                    colors = listOf(

                        Color(0xFF050505),

                        Color(0xFF0A0A0A),

                        Color(0xFF101418),

                        Color(0xFF080808)
                    )
                )
            )
    )
}