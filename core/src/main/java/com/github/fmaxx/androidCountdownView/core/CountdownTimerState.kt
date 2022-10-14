package com.github.fmaxx.androidCountdownView.core

/**
 * Created by Maxim Firsov on 14.10.2022.
 * firsoffmaxim@gmail.com
 */
sealed interface CountdownTimerState
data class Running(val startTimeMillis: Long, val elapsedTimeMillis: Long): CountdownTimerState
data class Paused(val elapsedTimeMillis: Long): CountdownTimerState
data class Finished(val startTimeMillis: Long): CountdownTimerState