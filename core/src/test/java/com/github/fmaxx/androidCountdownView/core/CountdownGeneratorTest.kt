package com.github.fmaxx.androidCountdownView.core

import kotlinx.coroutines.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Created by Maxim Firsov on 11.10.2022.
 * firsoffmaxim@gmail.com
 */
internal class CountdownGeneratorTest {

    @Test
    fun testStates() {
        val generator = generator(1_000)

        generator.start()
        assertTrue(generator.isPlaying)

        generator.pause()
        assertFalse(generator.isPlaying)

        generator.resume()
        assertTrue(generator.isPlaying)

        generator.stop()
        assertFalse(generator.isPlaying)

        generator.restart()
        assertTrue(generator.isPlaying)

        generator.stop()
        assertFalse(generator.isPlaying)
    }

    @Test
    fun exampleTest() = runBlocking {
        val duration = 3_000L
        launch {
            val generator = generator(duration)
            generator.start()
            delay(duration + 100)
        }.join()
    }

    private fun generator(ms: Long) =
            CountdownGenerator(
                    unit = TimeUnit.MILLISECONDS,
                    duration = Duration.ofMillis(ms)
            )
}