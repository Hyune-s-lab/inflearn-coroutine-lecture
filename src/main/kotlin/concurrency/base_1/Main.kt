package concurrency.base_1

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
//        val oneResult = one()
//        val twoResult = two()
//        println("one: $oneResult, two: $twoResult")

        val one = async { one() }
        val two = async { two() }
        println("one: ${one.await()}, two: ${two.await()}")
    }

    println("Completed in $time ms")
}
