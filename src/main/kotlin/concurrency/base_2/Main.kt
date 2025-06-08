package concurrency.base_2

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

suspend fun one(): Int {
    delay(1000L)
    return 13
}

suspend fun two(): Int {
    delay(1000L)
    return 29
}

fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { one() }
        val two = async(start = CoroutineStart.LAZY) { two() }

        one.start()
        two.start()
        println("one: ${one.await()}, two: ${two.await()}")
    }

    println("Completed in $time ms")
}
