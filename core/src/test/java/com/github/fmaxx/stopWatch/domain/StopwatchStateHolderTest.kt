package com.github.fmaxx.stopWatch.domain

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by Maxim Firsov on 31.10.2022.
 * firsoffmaxim@gmail.com
 */
internal class StopwatchStateHolderTest {
    private val timestampProvider: TimestampProvider = mockk()
    private val elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider)
    private val stopwatchStateCalculator = StopwatchStateCalculator(
            timestampProvider = timestampProvider,
            elapsedTimeCalculator = elapsedTimeCalculator
    )
    private val timestampMillisecondsFormatter = TimestampMillisecondsFormatter()
    private lateinit var systemUnderTest: StopwatchStateHolder

    @BeforeEach
    fun setUp() {
        systemUnderTest = StopwatchStateHolder(
                stateCalculator = stopwatchStateCalculator,
                timeCalculator = elapsedTimeCalculator,
                formatter = timestampMillisecondsFormatter
        )
    }

    @Nested
    inner class Started {
        @Test
        fun `correctly formatted after no time passes`() {
            zeroTimePasses()
            systemUnderTest.start()
            val result = systemUnderTest.timeRepresentation
            expectThat(result).isEqualTo("00:00:000")
        }

        @Test
        fun `correctly formatted after some time passes from start`() {
            timePassesAfterStart(49999)
            systemUnderTest.start()
            val result = systemUnderTest.timeRepresentation
            expectThat(result).isEqualTo("00:49:999")
        }
    }

    @Nested
    inner class Paused {
        @Test
        fun `correctly formatted after no time passes`() {
            zeroTimePasses()
            systemUnderTest.start()
            systemUnderTest.pause()
            val result = systemUnderTest.timeRepresentation
            expectThat(result).isEqualTo("00:00:000")
        }

        @Test
        fun `correctly formatted after some time passes from start`() {
            timePassesAfterStart(49999)
            systemUnderTest.start()
            systemUnderTest.pause()
            val result = systemUnderTest.timeRepresentation
            expectThat(result).isEqualTo("00:49:999")
        }
    }

    private fun zeroTimePasses() {
        every { timestampProvider.milliseconds } returns 0
    }

    private fun timePassesAfterStart(amount: Long) {
        every { timestampProvider.milliseconds }
                .returnsMany(
                        listOf(
                                0L,
                                amount,
                        )
                )
    }
}