package com.is200.launcher.ui.components.mediaoverlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.data.providers.MediaControllerProvider
import com.is200.launcher.ui.theme.BorderColor
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

private enum class MediaTab {

    QUEUE,
    PLAYLISTS,
    ALBUMS
}
@Composable
private fun MediaTabButton(

    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(

                if (selected) {

                    BorderColor.copy(alpha = 0.22f)

                } else {

                    Color(0xFF121417)
                }
            )
            .border(
                width = 1.dp,

                color =

                    if (selected) {

                        BorderColor.copy(alpha = 0.38f)

                    } else {

                        BorderColor.copy(alpha = 0.12f)
                    },

                shape = RoundedCornerShape(18.dp)
            )
            .clickable {

                onClick()
            }
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp
            )
    ) {

        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MediaQueueItem(

    title: String,
    artist: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Color(0xFF121417)
            )
            .border(
                width = 1.dp,
                color = BorderColor.copy(alpha = 0.14f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = artist,
            color = BorderColor,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun MediaPlaylistItem(

    title: String,
    tracks: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Color(0xFF121417)
            )
            .border(
                width = 1.dp,
                color = BorderColor.copy(alpha = 0.14f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(

                        colors = listOf(

                            Color(0xFF1D2C3A),

                            Color(0xFF101317)
                        )
                    )
                )
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column {

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = tracks,
                color = BorderColor,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun MediaAlbumItem(

    title: String,
    artist: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Color(0xFF121417)
            )
            .border(
                width = 1.dp,
                color = BorderColor.copy(alpha = 0.14f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Color(0xFF181818)
                )
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column {

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = artist,
                color = BorderColor,
                fontSize = 13.sp
            )
        }
    }
}
@Composable
fun MediaOverlay(

    visible: Boolean
) {

    val context = LocalContext.current

    val mediaInfo =
        remember {

            mutableStateOf(
                MediaControllerProvider
                    .getCurrentMediaInfo(context)
            )
        }
    var selectedTab by remember {

        mutableStateOf(
            MediaTab.QUEUE
        )
    }

    var tabsExpanded by remember {

        mutableStateOf(false)
    }

    val listState =
        rememberLazyListState()

    val coroutineScope =
        rememberCoroutineScope()

    LaunchedEffect(Unit) {

        while (true) {

            mediaInfo.value =
                MediaControllerProvider
                    .getCurrentMediaInfo(context)

            delay(500)
        }
    }

    AnimatedVisibility(

        visible = visible,

        enter =

            slideInHorizontally(

                initialOffsetX = {

                    it
                }

            ) + fadeIn(),

        exit =

            slideOutHorizontally(

                targetOffsetX = {

                    it
                }

            ) + fadeOut()
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(420.dp)
                .padding(
                    top = 12.dp,
                    bottom = 12.dp
                )
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(38.dp),
                    ambientColor = BorderColor.copy(alpha = 0.18f),
                    spotColor = BorderColor.copy(alpha = 0.12f)
                )
                .clip(RoundedCornerShape(38.dp))
                .background(

                    Brush.verticalGradient(

                        colors = listOf(

                            Color(0xFF161A1F),
                            Color(0xFF0E1115)
                        )
                    )
                )
                .border(
                    width = 1.4.dp,
                    color = BorderColor.copy(alpha = 0.24f),
                    shape = RoundedCornerShape(38.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(26.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(
                                Color(0xFF1ED760)
                            )
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "NOW PLAYING",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )


                mediaInfo.value.artwork?.let { artwork ->

                    Image(

                        bitmap =
                            artwork.asImageBitmap(),

                        contentDescription = null,

                        contentScale =
                            ContentScale.Crop,

                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(30.dp))
                    )

                } ?: run {

                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                Color(0xFF181818)
                            )
                    )
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Text(
                    text = mediaInfo.value.title,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = mediaInfo.value.artist,
                    color = BorderColor,
                    fontSize = 18.sp,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(34.dp)
                )

                Box {

                    MediaTabButton(

                        title =

                            when (selectedTab) {

                                MediaTab.QUEUE ->
                                    "QUEUE"

                                MediaTab.PLAYLISTS ->
                                    "PLAYLISTS"

                                MediaTab.ALBUMS ->
                                    "ALBUMS"
                            },

                        selected = true
                    ) {

                        tabsExpanded =
                            !tabsExpanded
                    }

                    androidx.compose.animation.AnimatedVisibility(

                        visible = tabsExpanded,

                        enter =

                            slideInHorizontally(

                                initialOffsetX = {

                                    it / 2
                                }

                            ) + fadeIn(),

                        exit =

                            slideOutHorizontally(

                                targetOffsetX = {

                                    it / 2
                                }

                            ) + fadeOut(),

                        modifier = Modifier
                            .offset(y = (-180).dp)
                    ) {

                        Column(

                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            MediaTabButton(

                                title = "QUEUE",

                                selected =
                                    selectedTab ==
                                            MediaTab.QUEUE
                            ) {

                                selectedTab =
                                    MediaTab.QUEUE

                                tabsExpanded = false

                                coroutineScope.launch {

                                    listState.animateScrollToItem(0)
                                }
                            }

                            MediaTabButton(

                                title = "PLAYLISTS",

                                selected =
                                    selectedTab ==
                                            MediaTab.PLAYLISTS
                            ) {

                                selectedTab =
                                    MediaTab.PLAYLISTS

                                tabsExpanded = false

                                coroutineScope.launch {

                                    listState.animateScrollToItem(0)
                                }
                            }

                            MediaTabButton(

                                title = "ALBUMS",

                                selected =
                                    selectedTab ==
                                            MediaTab.ALBUMS
                            ) {

                                selectedTab =
                                    MediaTab.ALBUMS

                                tabsExpanded = false

                                coroutineScope.launch {

                                    listState.animateScrollToItem(0)
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    when (selectedTab) {

                        MediaTab.QUEUE -> {

                            LazyColumn(

                                state = listState,

                                verticalArrangement =
                                    Arrangement.spacedBy(12.dp),

                                contentPadding =
                                    PaddingValues(
                                        bottom = 24.dp
                                    )
                            ) {

                                items(10) { index ->

                                    MediaQueueItem(

                                        title =
                                            "Next Track ${index + 1}",

                                        artist =
                                            "Unknown Artist"
                                    )
                                }
                            }
                        }

                        MediaTab.PLAYLISTS -> {

                            LazyColumn(

                                state = listState,

                                verticalArrangement =
                                    Arrangement.spacedBy(12.dp),

                                contentPadding =
                                    PaddingValues(
                                        bottom = 24.dp
                                    )
                            ) {

                                items(8) { index ->

                                    MediaPlaylistItem(

                                        title =
                                            "Playlist ${index + 1}",

                                        tracks =
                                            "${20 + index * 7} Tracks"
                                    )
                                }
                            }
                        }

                        MediaTab.ALBUMS -> {

                            LazyColumn(

                                state = listState,

                                verticalArrangement =
                                    Arrangement.spacedBy(12.dp),

                                contentPadding =
                                    PaddingValues(
                                        bottom = 24.dp
                                    )
                            ) {

                                items(6) { index ->

                                    MediaAlbumItem(

                                        title =
                                            "Album ${index + 1}",

                                        artist =
                                            "Artist ${index + 1}"
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}