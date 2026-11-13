package com.is200.launcher.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.ui.theme.AccentColor
import com.is200.launcher.ui.theme.BorderColor
import kotlinx.coroutines.delay
import androidx.compose.runtime.saveable.rememberSaveable
import com.is200.launcher.core.LauncherSession
    @Composable
    fun LexusLauncherRoot() {

        var showBootScreen by rememberSaveable {

            mutableStateOf(
                !LauncherSession.hasInitialized
            )
        }
        LaunchedEffect(Unit) {

            delay(3200)

            LauncherSession.hasInitialized = true

            showBootScreen = false        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            AnimatedVisibility(

                visible = showBootScreen,

                enter = fadeIn(
                    animationSpec = tween(600)
                ),

                exit = fadeOut(
                    animationSpec = tween(800)
                )
            ) {

                LexusBootScreen()
            }

            AnimatedVisibility(

                visible = !showBootScreen,

                enter = fadeIn(
                    animationSpec = tween(1200)
                ) + slideInVertically(

                    initialOffsetY = {

                        it / 12
                    }
                ),

                exit = fadeOut()
            ) {

                LexusLauncher()
            }
        }
    }

    @Composable
    fun LexusBootScreen() {

        val glowAlpha =
            remember {

                Animatable(0f)
            }

        LaunchedEffect(Unit) {

            glowAlpha.animateTo(

                targetValue = 1f,

                animationSpec = tween(
                    durationMillis = 1800
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(420.dp)
                    .background(

                        Brush.radialGradient(

                            colors = listOf(

                                AccentColor.copy(
                                    alpha =
                                        0.18f * glowAlpha.value
                                ),

                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier.align(Alignment.Center),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "LEXUS",

                    color = Color.White,

                    fontSize = 58.sp,

                    fontWeight = FontWeight.Bold,

                    letterSpacing = 6.sp
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "INITIALIZING SYSTEM",

                    color = BorderColor,

                    fontSize = 16.sp,

                    letterSpacing = 3.sp
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            Color(0x22FFFFFF)
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(
                                glowAlpha.value
                            )
                            .background(
                                AccentColor
                            )
                    )
                }
            }

            Text(
                text = "LEXUS IS200 INFOTAINMENT",

                color = Color.Gray,

                fontSize = 12.sp,

                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
            )
        }
    }
