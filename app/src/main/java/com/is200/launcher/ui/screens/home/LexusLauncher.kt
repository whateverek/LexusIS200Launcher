
package com.is200.launcher.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.is200.launcher.core.LauncherSession
import com.is200.launcher.core.cache.LauncherMemory
import com.is200.launcher.data.providers.AppsProvider
import com.is200.launcher.data.storage.PinnedAppsStorage
import com.is200.launcher.ui.components.background.LauncherBackground
import com.is200.launcher.ui.components.dock.DockBar
import com.is200.launcher.ui.components.dock.SideDock
import com.is200.launcher.ui.components.mediaoverlay.MediaOverlay
import com.is200.launcher.ui.components.navigation.FullscreenNavigationView
import com.is200.launcher.ui.components.navigation.MainMapArea
import com.is200.launcher.ui.components.topbar.TopBar
import com.is200.launcher.ui.navigation.LauncherScreen
import com.is200.launcher.ui.navigation.LauncherState
import com.is200.launcher.ui.screens.apps.AppsDrawerOverlay

@Composable
fun LexusLauncher() {

    val context = LocalContext.current

    var mediaOverlayVisible by remember {
        mutableStateOf(false)
    }

    val mapWidth by animateDpAsState(
        targetValue = if (mediaOverlayVisible) {
            440.dp
        } else {
            0.dp
        },
        animationSpec = spring(),
        label = ""
    )

    if (!LauncherMemory.hasLoadedApps) {

        LauncherMemory.installedApps.clear()

        LauncherMemory.installedApps.addAll(
            AppsProvider.getInstalledApps(context)
        )

        val savedPinnedPackageNames =
            PinnedAppsStorage.loadPinnedApps(context)

        val initialPinnedApps =
            if (savedPinnedPackageNames.isNotEmpty()) {
                LauncherMemory.installedApps.filter {
                    savedPinnedPackageNames.contains(it.packageName)
                }
            } else {
                LauncherMemory.installedApps.take(4)
            }

        LauncherMemory.pinnedApps.clear()

        LauncherMemory.pinnedApps.addAll(initialPinnedApps)

        LauncherMemory.hasLoadedApps = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            AnimatedContent(
                targetState = LauncherState.currentScreen,
                transitionSpec = {

                    if (!LauncherSession.hasRenderedHome) {

                        LauncherSession.hasRenderedHome = true

                        EnterTransition.None togetherWith ExitTransition.None

                    } else {

                        slideInHorizontally(
                            initialOffsetX = {
                                it / 12
                            }
                        ) + fadeIn(
                            initialAlpha = 0.92f
                        ) togetherWith
                                slideOutHorizontally(
                                    targetOffsetX = {
                                        -it / 18
                                    }
                                ) + fadeOut(
                            targetAlpha = 0.92f
                        )
                    }
                },
                label = ""
            ) { screen ->

                when (screen) {

                    LauncherScreen.HOME -> {

                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            LauncherBackground()

                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {

                                TopBar()

                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = Color(0xFF1A1A1A)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(20.dp)
                                ) {

                                    SideDock(
                                        pinnedApps = LauncherMemory.pinnedApps,

                                        onAppsClick = {
                                            LauncherState.currentScreen =
                                                LauncherScreen.APPS
                                        },

                                        onRemoveApp = { app ->

                                            val updated =
                                                LauncherMemory.pinnedApps
                                                    .toMutableList()

                                            updated.remove(app)

                                            LauncherMemory.pinnedApps.clear()

                                            LauncherMemory.pinnedApps.addAll(
                                                updated
                                            )

                                            PinnedAppsStorage.savePinnedApps(
                                                context,
                                                updated.map {
                                                    it.packageName
                                                }
                                            )
                                        }
                                    )

                                    Spacer(
                                        modifier = Modifier.width(20.dp)
                                    )

                                    Row(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Box(
                                            modifier = Modifier.weight(1f)
                                        ) {

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(
                                                        end = mapWidth
                                                    )
                                            ) {

                                                MainMapArea(
                                                    onOpenNavigation = {
                                                        LauncherState.currentScreen =
                                                            LauncherScreen.NAVIGATION
                                                    }
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.CenterEnd)
                                            ) {

                                                MediaOverlay(
                                                    visible = mediaOverlayVisible
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    LauncherScreen.APPS -> {

                        AppsDrawerOverlay(

                            onClose = {
                                LauncherState.currentScreen =
                                    LauncherScreen.HOME
                            },

                            onPinApp = { app ->

                                val current =
                                    LauncherMemory.pinnedApps
                                        .toMutableList()

                                current.removeAll {
                                    it.packageName == app.packageName
                                }

                                if (current.size >= 4) {
                                    current.removeAt(current.lastIndex)
                                }

                                current.add(0, app)

                                LauncherMemory.pinnedApps.clear()

                                LauncherMemory.pinnedApps.addAll(current)

                                PinnedAppsStorage.savePinnedApps(
                                    context,
                                    current.map {
                                        it.packageName
                                    }
                                )

                                LauncherState.currentScreen =
                                    LauncherScreen.HOME
                            }
                        )
                    }

                    LauncherScreen.NAVIGATION -> {

                        FullscreenNavigationView(
                            onExit = {
                                LauncherState.currentScreen =
                                    LauncherScreen.HOME
                            }
                        )
                    }
                }
            }
        }

        DockBar(
            onAppsClick = {
                LauncherState.currentScreen = LauncherScreen.APPS
            }
        )
    }
}
