package com.github.fmaxx.stopWatch.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Maxim Firsov on 31.10.2022.
 * firsoffmaxim@gmail.com
 */
class StopwatchProcessor(
        private val stateHolder: StopwatchStateHolder,
        private val scope: CoroutineScope,
        private val tickDelayMilliseconds: Long = 20
) {

    init {
        require(tickDelayMilliseconds > 0)
    }

    private var job: Job? = null
    private val _ticker = MutableStateFlow("")
    val ticker: StateFlow<String> = _ticker

    fun start() {
        if (job == null) startJob()
        stateHolder.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                _ticker.value = stateHolder.timeRepresentation
                delay(tickDelayMilliseconds)
            }
        }
    }

    fun pause() {
        stateHolder.pause()
        stopJob()
    }

    fun stop() {
        stateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun clearValue() {
        _ticker.value = ""
    }
}