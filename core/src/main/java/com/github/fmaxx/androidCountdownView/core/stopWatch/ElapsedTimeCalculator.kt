package com.github.fmaxx.androidCountdownView.core.stopWatch

/**
 * Created by Maxim Firsov on 14.10.2022.
 * firsoffmaxim@gmail.com
 */
class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider
) {
    fun calculate(state: Running): Long {
        val current = timestampProvider.milliseconds
        val passedSinceStart = if (current > state.startTime) {
            current - state.startTime
        } else {
            0
        }

        return passedSinceStart + state.elapsedTime
    }
}