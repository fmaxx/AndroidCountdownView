package com.github.fmaxx.androidCountdownView.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import java.time.Duration
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll

/**
 * Created by Maxim Firsov on 11.10.2022.
 * firsoffmaxim@gmail.com
 */
//@OptIn(ExperimentalCoroutinesApi::class)
//internal class CountdownGeneratorTest {
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(UnconfinedTestDispatcher())
//    }
//
//    @After
//    fun teardown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun testStates() {
//        val generator = generator(1_000)
//
//        generator.start()
//        assertTrue(generator.isPlaying)
//
//        generator.pause()
//        assertFalse(generator.isPlaying)
//
//        generator.resume()
//        assertTrue(generator.isPlaying)
//
//        generator.stop()
//        assertFalse(generator.isPlaying)
//
//        generator.restart()
//        assertTrue(generator.isPlaying)
//
//        generator.stop()
//        assertFalse(generator.isPlaying)
//    }
//
//    @Test
//    fun exampleTest() = runBlocking {
//        val duration = 1_000L
//        val results = mutableListOf<CountdownEvent>()
//        launch {
//            val generator = generator(duration)
//            generator.start()
//            generator.tick.toList(results)
//            delay(duration + 100)
//        }.join()
//        assertTrue(results.isNotEmpty())
//    }
//
//    @Test
//    fun flowTest() = runTest {
//        val results = mutableListOf<CountdownEvent>()
//        val millis = 2_000L
//        val duration = Duration.ofMillis(millis)
//        val generator = generator(millis)
//
//
//        generator.start()
////        generator.tick.toList(results)
//       /* generator.tick.stateIn(this, SharingStarted.Eagerly, null).filterNotNull().collect{
//            println("added: $it")
//            results.add(it)
//        }*/
////        runCurrent()
//        generator.tick.collect{
//            results.add(it)
//        }
//        delay(millis + 100)
////        job.cancel()
////        assertTrue(results.isNotEmpty())
////        println("~~~ results: $results")
//    }
//
//    @Test
//    fun flowTest2() = runTest {
//        data class TickEventCase(val progress: Float, val expected: CountdownEvent)
//        val results = mutableListOf<CountdownEvent>()
//        val millis = 20_000L
//        val duration = Duration.ofMillis(millis)
//        val generator = generator(millis)
//        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
//            generator.tick.toList(results)
//        }
//        val input = listOf(
//                TickEventCase(0f, Start(CountdownInfo(0f, total = duration))),
//                TickEventCase(0.1f, Progress(CountdownInfo(0.1f, total = duration))),
//                TickEventCase(0.8f, Progress(CountdownInfo(0.8f, total = duration))),
//                TickEventCase(1f, Finish(CountdownInfo(1f, total = duration))),
//        )
//        input.forEach {
//            generator.tickProgress(it.progress)
//        }
//        runCurrent() // Important
//
//        assert(Start(CountdownInfo(0f, total = Duration.ofMillis(millis))) ==
//                Start(CountdownInfo(0f, total = Duration.ofMillis(millis))))
//        assertEquals(input.size, results.size)
//        results.forEachIndexed { index, event ->
//            assert(input[index].expected == event)
//        }
//        job.cancel() // Important
//    }
//
//    private fun generator(ms: Long) =
//            CountdownGenerator(
//                    unit = TimeUnit.MILLISECONDS,
//                    duration = Duration.ofMillis(ms)
//            )
//}
//
//class MyViewModel(private val dispatcher: CoroutineDispatcher) : ViewModel() {
//
//    private val _numbers = MutableStateFlow(0)
//    val numbers: StateFlow<Int> = _numbers
//
//    fun addNumber(number: Int) {
//        viewModelScope.launch(dispatcher) {
//            _numbers.value = number
//        }
//    }
//}