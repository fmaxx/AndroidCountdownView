package com.github.fmaxx.androidCountdownView.core

import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Created by Maxim Firsov on 10.10.2022.
 * firsoffmaxim@gmail.com
 */
data class CountdownGeneratorParams(
        val duration: Duration,
        val timeUnit: TimeUnit
)
