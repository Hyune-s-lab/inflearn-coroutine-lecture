package cancel.base_2

import kotlinx.coroutines.*

fun main() = runBlocking {
    val startTIme = System.currentTimeMillis()

    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTIme
        var i = 0

        while (i < 5) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }

    delay(3000L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}
