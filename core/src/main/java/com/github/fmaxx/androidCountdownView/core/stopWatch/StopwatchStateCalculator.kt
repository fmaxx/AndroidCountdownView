package com.github.fmaxx.androidCountdownView.core.stopWatch

/**
 * Created by Maxim Firsov on 14.10.2022.
 * firsoffmaxim@gmail.com
 */
class StopwatchStateCalculator(
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator
) {
    fun calculateRunningState(oldState: StopwatchState): Running =
        when (oldState) {
            is Paused -> {
                Running(
                    startTime = timestampProvider.milliseconds,
                    elapsedTime = oldState.elapsedTime
                )
            }
            is Running -> oldState
        }

    fun calculatePausedState(oldState: StopwatchState): Paused {
        return when (oldState) {
            is Paused -> oldState
            is Running -> {
                val elapsedTime = elapsedTimeCalculator.calculate(oldState)
                Paused(elapsedTime)
            }
        }
    }
}