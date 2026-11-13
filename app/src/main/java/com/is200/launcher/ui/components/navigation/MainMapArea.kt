package com.is200.launcher.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.ui.theme.BorderColor
import com.is200.launcher.ui.components.media.SpotifyNavigationOverlay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.is200.launcher.maps.NavigationManager
import com.is200.launcher.maps.RealMapView

@Composable
    fun MainMapArea(
        onOpenNavigation: () -> Unit
    ) {

        var searchQuery by remember {

            mutableStateOf("")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp)
                .clip(RoundedCornerShape(38.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0B0F14),
                            Color(0xFF10161D),
                            Color(0xFF090C10)
                        )
                    )
                )
                .border(
                    width = 1.4.dp,
                    color = BorderColor.copy(alpha = 0.42f),
                    shape = RoundedCornerShape(38.dp)
                )

        ) {

            RealMapView()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(

                            colors = listOf(

                                Color.Black.copy(alpha = 0.22f),

                                Color.Transparent,

                                Color.Black.copy(alpha = 0.32f)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(38.dp))
                    .border(
                        width = 2.dp,
                        color = BorderColor.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(38.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(38.dp))
                    .background(
                        Brush.radialGradient(

                            colors = listOf(

                                BorderColor.copy(alpha = 0.07f),

                                Color.Transparent
                            ),

                            radius = 1600f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(34.dp)
            ) {

                Column {

                    Text(
                        text = "LIVE NAVIGATION",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    NavigationSearchBar(

                        value = searchQuery,

                        onValueChange = {

                            searchQuery = it

                            NavigationManager.searchLocation(it)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    NavigationSearchResults(

                        results =
                            NavigationManager.searchResults,

                        onLocationClick = { location ->

                            searchQuery =
                                location.display_name

                            NavigationManager.selectLocation(
                                location
                            )
                        }
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(28.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Color(0xCC101010)
                        )
                        .border(
                            width = 1.dp,
                            color = BorderColor.copy(alpha = 0.24f),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable {

                            onOpenNavigation()
                        }
                        .padding(horizontal = 22.dp),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "OPEN NAVIGATION",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    @Composable
    fun FullscreenNavigationView(
        onExit: () -> Unit
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            RealMapView()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(

                            colors = listOf(

                                Color.Black.copy(alpha = 0.22f),

                                Color.Transparent,

                                Color.Black.copy(alpha = 0.36f)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(28.dp)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xCC101010)
                    )
                    .border(
                        width = 1.dp,
                        color = BorderColor.copy(alpha = 0.30f),
                        shape = CircleShape
                    )
                    .clickable {

                        onExit()
                    },

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 28.dp,
                        end = 28.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                NavigationActionButton(
                    text = "+"
                )

                NavigationActionButton(
                    text = "−"
                )

                NavigationActionButton(
                    text = "◎"
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 28.dp)
                    .height(62.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Color(0xCC101010)
                    )
                    .border(
                        width = 1.dp,
                        color = BorderColor.copy(alpha = 0.24f),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 26.dp),

                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                Color(0xFF59FF87)
                            )
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "GPS ACTIVE",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

             SpotifyNavigationOverlay(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            )
        }
    }
    @Composable
    fun NavigationActionButton(
        text: String,
        onClick: () -> Unit = {}
    ) {

        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Color(0xCC101010)
                )
                .border(
                    width = 1.dp,
                    color = BorderColor.copy(alpha = 0.26f),
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable {

                    onClick()
                },

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = text,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
