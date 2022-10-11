package com.github.fmaxx.androidCountdownView.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*

/**
 * Created by Maxim Firsov on 10.10.2022.
 * firsoffmaxim@gmail.com
 */
class CountdownGenerator(unit: TimeUnit,
                         private val duration: Duration,
                         dispatcher: CoroutineDispatcher = Dispatchers.Default) {
    private var _progress = 0f
    private var totalMilliseconds = duration.toMillis()
    private var currentMilliseconds = 0L
    private var delayMilliseconds = getDelayMilliseconds(unit)
    private var _isPlaying = false
    val isPlaying get() = _isPlaying
    private val _tick = MutableStateFlow<CountdownEvent>(Start(getCurrentInfo()))
    private var job: Job? = null
    private val scope = CoroutineScope(dispatcher)
    private var lastTimeMilliseconds = 0L
    val tick: StateFlow<CountdownEvent> get() = _tick
    val progress get() = _progress

    fun restart() {
        stop()
        start()
    }

    fun start() {
        currentMilliseconds = 0
        _progress = 0f
        lastTimeMilliseconds = systemTimeMilliseconds
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
        if (!_isPlaying) return
        _isPlaying = false
        cancelJob()
    }

    fun resume() {
        if (_isPlaying) return
        _isPlaying = true
        lastTimeMilliseconds = systemTimeMilliseconds
        restartJob()
    }

    fun stop() {
        _progress = 1f
        currentMilliseconds = totalMilliseconds
        pause()
    }

    private fun cancelJob() {
        job?.cancelChildren()
        job = null
    }

    private fun startJob() {
        job = scope.launch {
            while (_isPlaying && isActive) {
                val event = when {
                    _progress == 0f -> Start(getCurrentInfo())
                    _progress > 0f && _progress < 1f -> Progress(getCurrentInfo())
                    else -> Finish(getCurrentInfo())
                }

                if (_progress > 1) {
                    cancelJob()
                    return@launch
                }

                println("event: $event")
//                println("currentMilliseconds: $currentMilliseconds")
//                println("delta: ${(systemTimeMilliseconds - lastTimeMilliseconds)}")
//                println("-----------------")
                _tick.tryEmit(event)
                currentMilliseconds += (systemTimeMilliseconds - lastTimeMilliseconds)
                _progress = currentMilliseconds.toFloat() / totalMilliseconds.toFloat()
                lastTimeMilliseconds = systemTimeMilliseconds
                delay(delayMilliseconds)
            }
        }
    }

    private fun restartJob() {
        cancelJob()
        startJob()
    }

    private val systemTimeMilliseconds get() = System.currentTimeMillis()
}

data class CountdownInfo(val progress: Float, val current: Duration, val total: Duration)

