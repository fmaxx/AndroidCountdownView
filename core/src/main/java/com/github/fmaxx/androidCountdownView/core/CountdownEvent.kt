package com.github.fmaxx.androidCountdownView.core

/**
 * Created by Maxim Firsov on 11.10.2022.
 * firsoffmaxim@gmail.com
 */
sealed interface CountdownEvent

data class Start(val info: CountdownInfo) : CountdownEvent
data class Progress(val info: CountdownInfo) : CountdownEvent
data class Finish(val info: CountdownInfo) : CountdownEvent