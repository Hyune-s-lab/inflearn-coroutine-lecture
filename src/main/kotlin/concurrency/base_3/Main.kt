package concurrency.base_3

import kotlinx.coroutines.*

suspend fun one(): Int {
    delay(1000L)
    return 13
}

suspend fun two(): Int {
    delay(1000L)
    return 29
}

@OptIn(DelicateCoroutinesApi::class)
fun somethingOne() = GlobalScope.async { one() }

@OptIn(DelicateCoroutinesApi::class)
fun somethingTwo() = GlobalScope.async { two() }

fun main() = runBlocking {
    val one = somethingOne()
    val two = somethingTwo()
}

suspend fun sum(): Int = coroutineScope {
    val one = async { one() }
    val two = async { two() }
    one.await() + two.await()
}
