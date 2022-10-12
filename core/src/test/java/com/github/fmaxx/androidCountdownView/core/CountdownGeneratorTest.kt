package com.github.fmaxx.androidCountdownView.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Created by Maxim Firsov on 11.10.2022.
 * firsoffmaxim@gmail.com
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class CountdownGeneratorTest {

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

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

    @Test
    fun flowTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = MyViewModel(dispatcher)
        val results = mutableListOf<Int>()
        val job = launch(dispatcher) { viewModel.numbers.toList(results) }

        for(i in 0..1000){
            viewModel.addNumber(i)
        }

//        viewModel.addNumber(8)
        runCurrent() // Important

        assertEquals(1001, results.size)
//        assertThat(results).isEqualTo(listOf(0, 5, 8))
        job.cancel() // Important

        /*val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val results = mutableListOf<CountdownEvent>()
        val duration = 200L
        val generator = generator(duration)
        val job = launch(dispatcher) {
            generator.start()
            runCurrent()
            generator.tick.toList(results)
            delay(duration + 100)
        }


        //Verify
        assertThat(results.first(), CoreMatchers.instanceOf(Start::class.java))
        assertTrue(results.count { it is Start } == 1)
        val ttt = results.count { it is Finish }
        println("ttt: $ttt")
        assertTrue(results.count { it is Finish } == 1)
//        assertEquals(listOf(0, 1, 2, 3, 4, 5), results)
        job.cancel()*/
    }

    @Test
    fun flowTest2() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = MyViewModel(dispatcher)
        val results = mutableListOf<CountdownEvent>()
        val duration = 20_000L
        val generator = generator(duration)
        val job = launch(dispatcher) {
//            generator.start()
            generator.tick.toList(results)
        }

        generator.tickEvent(0f)
        generator.tickEvent(0.1f)
        generator.tickEvent(0.5f)
        generator.tickEvent(1f)

        runCurrent() // Important

//        assertEquals(1001, results.size)
//        assertThat(results).isEqualTo(listOf(0, 5, 8))
        job.cancel() // Important

        /*val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val results = mutableListOf<CountdownEvent>()

        val generator = generator(duration)
        val job = launch(dispatcher) {
            generator.start()
            runCurrent()
            generator.tick.toList(results)
            delay(duration + 100)
        }


        //Verify
        assertThat(results.first(), CoreMatchers.instanceOf(Start::class.java))
        assertTrue(results.count { it is Start } == 1)
        val ttt = results.count { it is Finish }
        println("ttt: $ttt")
        assertTrue(results.count { it is Finish } == 1)
//        assertEquals(listOf(0, 1, 2, 3, 4, 5), results)
        job.cancel()*/
    }

    private fun generator(ms: Long) =
            CountdownGenerator(
                    unit = TimeUnit.MILLISECONDS,
                    duration = Duration.ofMillis(ms)
            )
}

class MyViewModel(private val dispatcher: CoroutineDispatcher) : ViewModel() {

    private val _numbers = MutableStateFlow(0)
    val numbers: StateFlow<Int> = _numbers

    fun addNumber(number: Int) {
        viewModelScope.launch(dispatcher) {
            _numbers.value = number
        }
    }
}