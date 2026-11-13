package com.is200.launcher.data.providers

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager

data class MediaInfo(

    val title: String = "Nothing Playing",

    val artist: String = "",

    val isPlaying: Boolean = false,

    val duration: Long = 1L,

    val position: Long = 0L,

    val artwork: Bitmap? = null
)

object MediaControllerProvider {

    private fun getController(
        context: Context
    ): MediaController? {

        return try {

            val mediaSessionManager =

                context.getSystemService(
                    Context.MEDIA_SESSION_SERVICE
                ) as MediaSessionManager

            val componentName =
                ComponentName(
                    context,
                    NotificationListener::class.java
                )

            val controllers =
                mediaSessionManager
                    .getActiveSessions(
                        componentName
                    )

            controllers.firstOrNull {

                it.packageName.contains(
                    "spotify"
                )
            } ?: controllers.firstOrNull()

        } catch (_: Exception) {

            null
        }
    }

    fun getCurrentMediaInfo(
        context: Context
    ): MediaInfo {

        return try {

            val controller =
                getController(context)

            val metadata =
                controller?.metadata

            val playbackState =
                controller?.playbackState

            val title =
                metadata?.getString(
                    MediaMetadata.METADATA_KEY_TITLE
                ) ?: "Nothing Playing"

            val artist =
                metadata?.getString(
                    MediaMetadata.METADATA_KEY_ARTIST
                ) ?: ""

            val isPlaying =
                playbackState?.state == 3

            val duration =
                metadata?.getLong(
                    MediaMetadata.METADATA_KEY_DURATION
                ) ?: 1L

            val position =
                playbackState?.position ?: 0L

            val artwork =
                metadata?.getBitmap(
                    MediaMetadata.METADATA_KEY_ALBUM_ART
                )

            MediaInfo(

                title = title,

                artist = artist,

                isPlaying = isPlaying,

                duration = duration,

                position = position,

                artwork = artwork
            )

        } catch (_: Exception) {

            MediaInfo()
        }
    }

    fun playPause(
        context: Context
    ) {

        try {

            val controller =
                getController(context)

            val isPlaying =
                controller?.playbackState?.state == 3

            if (isPlaying) {

                controller
                    ?.transportControls
                    ?.pause()

            } else {

                controller
                    ?.transportControls
                    ?.play()
            }

        } catch (_: Exception) {

        }
    }

    fun next(
        context: Context
    ) {

        try {

            getController(context)
                ?.transportControls
                ?.skipToNext()

        } catch (_: Exception) {

        }
    }

    fun previous(
        context: Context
    ) {

        try {

            val controller =
                getController(context)

            val controls =
                controller?.transportControls

            val position =
                controller?.playbackState?.position ?: 0L

            if (position > 5000L) {

                controls?.seekTo(0)

                controls?.play()

            } else {

                controls?.skipToPrevious()
            }

        } catch (_: Exception) {

        }
    }
}