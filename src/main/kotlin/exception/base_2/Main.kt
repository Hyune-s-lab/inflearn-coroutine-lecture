package exception.base_2

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("coroutine 1 started")

    launch {
        println("coroutine 2 started")
        delay(500L)
        println("coroutine 2 finished")
    }

    launch {
        println("coroutine 3 started")
        delay(500L)
        println("coroutine 3 finished")
    }

    val exceptionHandler = CoroutineExceptionHandler { _, msg ->
        println("coroutine handled $msg")
    }

    supervisorScope {
        launch {
            println("coroutine 4 started")

            launch {
                println("coroutine 5 started")
                delay(500L)
                println("coroutine 5 finished")
            }

            delay(200L)
            throw RuntimeException("coroutine 4 exception")
        }
    }

    delay(1000L)
    println("coroutine 1 finished")
}
