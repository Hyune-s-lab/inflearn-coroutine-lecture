package cancel.base_1

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("Im sleeping $i ...")
            delay(500L)
        }
    }

    delay(1300L)
    println("I'm tired of waiting!")

//    job.cancel()
//    job.join()

    job.cancelAndJoin()
    println("out")
}
