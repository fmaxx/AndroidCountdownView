package com.github.fmaxx.stopWatch.domain

/**
 * Created by Maxim Firsov on 31.10.2022.
 * firsoffmaxim@gmail.com
 */
class StopwatchStateHolder(private val stateCalculator: StopwatchStateCalculator,
                                    private val timeCalculator: ElapsedTimeCalculator,
                                    private val formatter: TimestampMillisecondsFormatter) {

    var currentState: StopwatchState = Paused(0)
        private set

    fun start() {
        currentState = stateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = Paused(0)
    }

    val timeRepresentation: String get() {
        val elapsedTime = when (val currentState = currentState) {
            is Paused -> currentState.elapsedTime
            is Running -> timeCalculator.calculate(currentState)
        }
        return formatter.format(elapsedTime)
    }
}