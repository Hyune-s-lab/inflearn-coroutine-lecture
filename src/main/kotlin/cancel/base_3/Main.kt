package cancel.base_3

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        repeat(5) { i ->
            try {
                println("job: I'm sleeping $i ...")
                delay(500L)
            } catch (e: CancellationException) {
                println("job: I'm cancelled!")
                throw e
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}
