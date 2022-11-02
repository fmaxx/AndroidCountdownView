package com.github.fmaxx.stopWatch.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

/**
 * Created by Maxim Firsov on 31.10.2022.
 * firsoffmaxim@gmail.com
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StopwatchProcessorTest {

    private val stopwatchStateHolder: StopwatchStateHolder = mockk(relaxed = true) {
        every { timeRepresentation } returns ""
    }

    @Test
    fun `Initially the stopwatch value is empty`() = runTest {
        val sut = processor {
            scope(testScheduler)
        }
        val result = sut.ticker.first()
        expectThat(result).isEmpty()
    }

    @Test
    fun `After starting a stopwatch its value is emitted`() = runTest {
        givenStateHolderReturnsTime("0")
        val sut = processor {
            scope(testScheduler)
        }
        sut.start()
        runCurrentBy(1)
        val result = sut.ticker.first()
        expectThat(result).isEqualTo("0")
        sut.stop()
    }

    @Test
    fun `When a stopwatch is running its value updated accordingly`() = runTest {
        givenStateHolderReturnsTime("0", "1", "2")
        val sut = processor {
            scope(testScheduler)
        }
        sut.start()
        runCurrentBy(5)
        expectThat(sut.ticker.first()).isEqualTo("2")
        sut.stop()
    }

    @Test
    fun `When a stopwatch is stopped the value is set to an empty string`() = runTest {
        givenStateHolderReturnsTime("0")
        val sut = processor {
            scope(testScheduler)
        }
        sut.start()
        runCurrentBy(1)
        sut.stop()
        runCurrentBy(1)
        val result = sut.ticker.first()
        expectThat(result).isEmpty()
    }

    private fun TestScope.runCurrentBy(delayTimeMillis: Long) {
        advanceTimeBy(delayTimeMillis)
        runCurrent()
    }

    private fun processor(
            tickDelayMs: Long = 1,
            block: () -> CoroutineScope): StopwatchProcessor =
            StopwatchProcessor(
                    stateHolder = stopwatchStateHolder,
                    scope = block(),
                    tickDelayMilliseconds = tickDelayMs
            )

    private fun scope(scheduler: TestCoroutineScheduler): CoroutineScope =
            CoroutineScope(StandardTestDispatcher(scheduler))

    private fun givenStateHolderReturnsTime(vararg timeString: String) {
        every { stopwatchStateHolder.timeRepresentation } returnsMany listOf(*timeString)
    }


}