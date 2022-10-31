package com.github.fmaxx.stopWatch.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.DescribeableBuilder
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
//    private val coroutineDispatcher = StandardTestDispatcher()
    private val coroutineDispatcher = UnconfinedTestDispatcher()
//    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(SupervisorJob() + coroutineDispatcher)
    private lateinit var systemUnderTest: StopwatchProcessor

    @BeforeEach
    fun setUp() {
        systemUnderTest = StopwatchProcessor(
                stateHolder = stopwatchStateHolder,
                scope = coroutineScope
        )
    }

    @Test
    fun `Initially the stopwatch value is empty`() = runTest {
        delay(1000)
        val result = systemUnderTest.ticker.first()
        expectThat(result).isEmpty()
    }

    @Test
    fun `After starting a stopwatch its value is emitted`() = runTest {
        givenStateHolderReturnsTime("0")
        systemUnderTest.start()
        delay(1000)
        val result = systemUnderTest.ticker.first()
        expectThat(result).isEqualTo("0")
    }

    @Test
    fun `When a stopwatch is running its value updated accordingly`() = runTest {
        givenStateHolderReturnsTime("0", "1", "2", "3", "4", "5")
        systemUnderTest.start()
//        coroutineDispatcher.advanceTimeBy(1000)
        apply {
            advanceTimeBy(10_000)
            runCurrent()
        }

//        delay(1000)

        val result = systemUnderTest.ticker.first()
        expectThat(result).isEqualTo("5")
    }

/*




    @Test
    fun `When a stopwatch is paused its value does not change`() = runBlockingTest {
        givenStateHolderReturnsTime("0", "1", "2", "3", "4", "5")
        systemUnderTest.start()
        coroutineDispatcher.advanceTimeBy(50)
        systemUnderTest.pause()

        val result = systemUnderTest.ticker.first()

        expectThat(result).isEqualTo("2")
    }

    @Test
    fun `When a stopwatch is stopped the value is set to an empty string`() = runBlockingTest {
        givenStateHolderReturnsTime("0")
        systemUnderTest.start()
        coroutineDispatcher.advanceTimeBy(1000)
        systemUnderTest.stop()
        coroutineDispatcher.advanceTimeBy(1000)

        val result = systemUnderTest.ticker.first()

        expectThat(result).isEmpty()
    }

    @Nested
    inner class Coroutines {

        @Test
        fun `Initially the scope is inactive`() {
            coroutineDispatcher.advanceTimeBy(1000)

            expectThat(coroutineScope).hasChildrenCount(0)
        }

        @Test
        fun `When first task is stared the scope becomes active`() {
            systemUnderTest.start()

            coroutineDispatcher.advanceTimeBy(1000)
            expectThat(coroutineScope).hasChildrenCount(1)
        }

        @Test
        fun `When last task is stopped the scope becomes inactive`() {
            systemUnderTest.start()
            coroutineDispatcher.advanceTimeBy(1000)

            systemUnderTest.stop()

            expectThat(coroutineScope).hasChildrenCount(0)
        }

        @Test
        fun `Starting another stopwatch after stopping the last works correctly`() {
            systemUnderTest.start()
            coroutineDispatcher.advanceTimeBy(1000)
            systemUnderTest.stop()

            systemUnderTest.start()

            expectThat(coroutineScope).hasChildrenCount(1)
        }

        @Test
        fun `When every stopwatch is paused the scope becomes inactive`() {
            every { stopwatchStateHolder.currentState }
                    .returns(Paused(0))
            systemUnderTest.start()

            systemUnderTest.pause()

            expectThat(coroutineScope).hasChildrenCount(0)
        }

        @Test
        fun `Resuming after every stopwatch is paused makes the scope active`() {
            every { stopwatchStateHolder.currentState }
                    .returns(Paused(0))
            systemUnderTest.start()
            systemUnderTest.pause()

            systemUnderTest.start()

            expectThat(coroutineScope).hasChildrenCount(1)
        }

        private fun DescribeableBuilder<CoroutineScope>.hasChildrenCount(count: Int) {
            val job = coroutineScope.coroutineContext.job
            expectThat(job.children.count()).isEqualTo(count)
        }
    }*/

    private fun givenStateHolderReturnsTime(vararg timeString: String) {
        every { stopwatchStateHolder.timeRepresentation } returnsMany listOf(*timeString)
    }


}