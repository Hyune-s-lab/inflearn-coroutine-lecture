package base.base_2

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        delay(1000L)
        println("world")
    }

    println("hello")
    job.join()  // Wait for the coroutine to finish
    println("done")
}
