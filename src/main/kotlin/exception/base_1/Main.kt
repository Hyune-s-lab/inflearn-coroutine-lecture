package exception.base_1

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("일반 job")

    val parent1 = CoroutineScope(Job())
    parent1.launch {
        launch {
            delay(50L)
            println("Job1 throwing exception")
            throw RuntimeException("Job1 exception")
        }

        launch {
            try {
                delay(200L)
                println("Job2 Done")
            } catch (e: CancellationException) {
                println("Job2 cancelled")
            }
        }
    }.join()

    delay(250L)

    println("\nsupervisor job")
    val parent2 = CoroutineScope(SupervisorJob())
    val job1 = parent2.launch {
        launch {
            delay(50L)
            println("Job1 throwing exception")
            throw RuntimeException("Job1 exception")
        }
    }

    val job2 = launch {
        try {
            delay(200L)
            println("Job2 Done")
        } catch (e: CancellationException) {
            println("Job2 cancelled")
        }
    }
}
