package com.bltech.moxtel.features.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@Composable
fun VideoView(movieId: Int, onFinish: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        VideoPlayer("https://assets.afcdn.com/video49/20210722/v_645516.m3u8", onFinish)
    }
}

@Composable
fun VideoPlayer(playlist: String, onFinish: () -> Unit) {
    val shouldShowLoading = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    val mediaSource = remember(playlist) {
        MediaItem.fromUri(playlist)
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        exoPlayer.apply {
            this.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            shouldShowLoading.value = true
                        }

                        Player.STATE_ENDED -> {
                            onFinish()
                        }

                        Player.STATE_IDLE -> {
                            //TODO: Ignore
                        }

                        Player.STATE_READY -> {
                            shouldShowLoading.value = false
                        }
                    }
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (exoPlayer.isPlaying.not()) {
                        exoPlayer.play()
                    }
                }

                Lifecycle.Event.ON_STOP -> {
                    // Pause the player when the Composable is in the background
                    exoPlayer.pause()
                }

                else -> {
                    // Nothing
                }
            }
        }
    })

    Box(
        modifier = Modifier.background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )

        if (shouldShowLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
