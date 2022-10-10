package com.github.fmaxx.androidCountdownView.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*

/**
 * Created by Maxim Firsov on 10.10.2022.
 * firsoffmaxim@gmail.com
 */
class CountdownGenerator(val unit: TimeUnit,
                         val duration: Duration) {

    private var _progress = 0f
    private var totalMilliseconds = 0L
    private var currentMilliseconds = 0L
    private var delayMilliseconds = 0L
    private var isPlaying = false
    private val _tick = MutableStateFlow(getCurrentInfo())
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    val tick = _tick
    val progress get() = _progress

    fun restart() {
        stop()
        start()
    }

    fun start() {
        totalMilliseconds = duration.toMillis()
        currentMilliseconds = 0
        _progress = 0f
        delayMilliseconds = getDelayMilliseconds(unit)
        if (totalMilliseconds <= 0) {
            stop()
        } else {
            resume()
        }
    }

    private fun getDelayMilliseconds(unit: TimeUnit): Long =
            when (unit) {
                NANOSECONDS,
                MICROSECONDS,
                MILLISECONDS -> 1
                SECONDS -> 1_000
                MINUTES -> 1_000 * 60
                HOURS -> 1_000 * 60 * 60
                DAYS -> 1_000 * 60 * 60 * 24
            }

    private fun getCurrentInfo(): CountdownInfo =
            CountdownInfo(
                    progress = _progress,
                    current = Duration.ofMillis(currentMilliseconds),
                    total = duration)

    fun pause() {
        if (!isPlaying) return
        isPlaying = false
        job?.cancelChildren()
    }

    fun resume() {
        job = scope.launch {
            while (isPlaying) {
                _tick.tryEmit(getCurrentInfo())
                delay(delayMilliseconds)
            }
        }
        if (isPlaying) return
        isPlaying = true
        scope
    }

    fun stop() {
        _progress = 1f
        pause()
    }
}

data class CountdownInfo(val progress: Float, val current: Duration, val total: Duration)

