package com.github.fmaxx.androidCountdownView.core.stopWatch

/**
 * Created by Maxim Firsov on 14.10.2022.
 * firsoffmaxim@gmail.com
 *
 * https://akjaw.com/kotlin-coroutine-flow-stopwatch-part1/
 */
sealed interface StopwatchState
data class Paused(val elapsedTime: Long) : StopwatchState
data class Running(val startTime: Long, val elapsedTime: Long) : StopwatchState