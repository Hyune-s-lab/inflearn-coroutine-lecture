package concurrency.base_4

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    try {
        sum()
    } catch (e: Exception) {
        println("ended in main")
    }
}

suspend fun sum(): Int = coroutineScope {
    val one = async {
        try {
            delay(Long.MAX_VALUE)
            println("delayed ended")
            42
        } finally {
            println("job canceled")
        }
    }

    val two = async<Int> {
        println("second job")
        throw ArithmeticException()
    }

    one.await() + two.await()
}
