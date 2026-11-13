package com.is200.launcher.ui.components.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.data.providers.MediaControllerProvider
import com.is200.launcher.ui.theme.BorderColor
import com.is200.launcher.ui.theme.SurfaceLight
import kotlinx.coroutines.delay

@Composable
fun BottomNowPlayingBar(

    onClick: () -> Unit
) {

    val context = LocalContext.current

    val mediaInfo =
        remember {

            mutableStateOf(
                MediaControllerProvider
                    .getCurrentMediaInfo(context)
            )
        }

    LaunchedEffect(Unit) {

        while (true) {

            mediaInfo.value =
                MediaControllerProvider
                    .getCurrentMediaInfo(context)

            delay(500)
        }
    }

    val progress =

        if (mediaInfo.value.duration > 0L) {

            (
                    mediaInfo.value.position.toFloat()
                            /
                            mediaInfo.value.duration.toFloat()
                    )
                .coerceIn(0f, 1f)

        } else {

            0f
        }

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {

                onClick()
            }
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(34.dp),
                ambientColor = BorderColor.copy(alpha = 0.12f),
                spotColor = BorderColor.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(34.dp))
            .border(
                width = 1.5.dp,
                color = BorderColor.copy(alpha = 0.34f),
                shape = RoundedCornerShape(34.dp)
            )
            .background(
                Brush.verticalGradient(

                    colors = listOf(

                        Color(0xFF161A1F),
                        Color(0xFF0E1115)
                    )
                )
            )
            .drawWithContent {

                drawRoundRect(

                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF161A1F),
                            Color(0xFF0E1115)
                        )
                    ),

                    cornerRadius =
                        androidx.compose.ui.geometry.CornerRadius(
                            34.dp.toPx(),
                            34.dp.toPx()
                        )
                )

                drawRoundRect(

                    color = BorderColor.copy(alpha = 0.08f),

                    size = size,

                    cornerRadius =
                        androidx.compose.ui.geometry.CornerRadius(
                            34.dp.toPx(),
                            34.dp.toPx()
                        ),

                    style =
                        androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 8f
                        )
                )

                drawRoundRect(

                    brush = Brush.horizontalGradient(

                        colors = listOf(

                            BorderColor.copy(alpha = 0.22f),

                            BorderColor.copy(alpha = 0.10f),

                            Color.Transparent
                        )
                    ),

                    size =
                        androidx.compose.ui.geometry.Size(

                            width = size.width * progress,

                            height = size.height
                        ),

                    cornerRadius =
                        androidx.compose.ui.geometry.CornerRadius(
                            34.dp.toPx(),
                            34.dp.toPx()
                        )
                )

                drawContent()
            }
            .padding(horizontal = 28.dp),

        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            mediaInfo.value.artwork?.let { artwork ->

                Image(

                    bitmap =
                        artwork.asImageBitmap(),

                    contentDescription = null,

                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(
                            width = 1.dp,
                            color = BorderColor.copy(alpha = 0.28f),
                            shape = RoundedCornerShape(22.dp)
                        )
                )

            } ?: run {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(

                            Brush.linearGradient(

                                listOf(
                                    Color(0xFF1D2C3A),
                                    Color(0xFF0F1115)
                                )
                            )
                        )
                )
            }

            Spacer(
                modifier = Modifier.width(18.dp)
            )

            Column {

                Text(
                    text = mediaInfo.value.title,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = mediaInfo.value.artist,
                    color = BorderColor,
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                MediaControlButton(
                    text = "⏮",
                    onClick = {

                        MediaControllerProvider
                            .previous(context)
                    }
                )

                MediaControlButton(
                    text =
                        if (mediaInfo.value.isPlaying) {
                            "⏸"
                        } else {
                            "▶"
                        },

                    onClick = {

                        MediaControllerProvider
                            .playPause(context)
                    }
                )

                MediaControlButton(
                    text = "⏭",
                    onClick = {

                        MediaControllerProvider
                            .next(context)
                    }
                )
            }
        }
    }
}

@Composable
fun MediaControlButton(
    text: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceLight)
            .clickable {

                onClick()
            },

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun SpotifyNavigationOverlay(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val mediaInfo =
        remember {

            mutableStateOf(
                MediaControllerProvider
                    .getCurrentMediaInfo(context)
            )
        }

    LaunchedEffect(Unit) {

        while (true) {

            mediaInfo.value =
                MediaControllerProvider
                    .getCurrentMediaInfo(context)

            delay(500)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
    ) {

        val progress =

            if (
                mediaInfo.value.duration > 0L &&
                mediaInfo.value.position >= 0L
            ) {

                (
                        mediaInfo.value.position.toFloat()
                                /
                                mediaInfo.value.duration.toFloat()
                        )
                    .coerceIn(0f, 1f)

            } else {

                0f
            }

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(36.dp))
                .background(
                    Color(0xE6050505)
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(36.dp))
                .background(
                    Brush.horizontalGradient(

                        colors = listOf(

                            BorderColor.copy(alpha = 0.22f),

                            Color.Transparent
                        ),

                        startX = 0f,

                        endX = maxOf(
                            1f,
                            1200f * progress
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(36.dp))
                .border(
                    width = 1.6.dp,
                    color = BorderColor.copy(alpha = 0.34f),
                    shape = RoundedCornerShape(36.dp)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.linearGradient(

                            colors = listOf(

                                Color(0xFF1B1B1B),

                                Color(0xFF0E0E0E)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = BorderColor.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(26.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "♫",
                    color = Color.White,
                    fontSize = 34.sp
                )
            }

            Spacer(
                modifier = Modifier.width(22.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = mediaInfo.value.title,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = mediaInfo.value.artist,
                    color = BorderColor,
                    fontSize = 18.sp,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            Color(0x22FFFFFF)
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                BorderColor
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(24.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                MediaControlButton(
                    text = "⏮",
                    onClick = {

                        MediaControllerProvider
                            .previous(context)
                    }
                )

                MediaControlButton(
                    text =
                        if (mediaInfo.value.isPlaying) {
                            "⏸"
                        } else {
                            "▶"
                        },

                    onClick = {

                        MediaControllerProvider
                            .playPause(context)
                    }
                )

                MediaControlButton(
                    text = "⏭",
                    onClick = {

                        MediaControllerProvider
                            .next(context)
                    }
                )
            }
        }
    }
}

@Composable
fun MiniNowPlaying() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF1D2C3A),
                            Color(0xFF0F1115)
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nightcall",
            color = Color.White,
            fontSize = 11.sp,
            maxLines = 1
        )

        Text(
            text = "Kavinsky",
            color = Color.Gray,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}