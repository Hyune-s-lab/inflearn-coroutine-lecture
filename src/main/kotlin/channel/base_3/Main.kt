package channel.base_3

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.sqrt

data class Job(val id: Int, val number: Int)
data class Result(val jobId: Int, val number: Int, val isPrime: Boolean)

fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    for (i in 2..sqrt(n.toDouble()).toInt()) {
        if (n % i == 0) return false
    }
    return true
}

fun main() = runBlocking {
    val jobChannel = Channel<Job>(Channel.UNLIMITED)
    val resultChannel = Channel<Result>(Channel.UNLIMITED)
    val consumerCount = 4
    val jobCount = 5

    launch {
        repeat(jobCount) { id ->
            val number = (10000..10100).random()
            println("Producing job=$id, number=$number")
            jobChannel.send(Job(id, number))
        }
        jobChannel.close()
    }

    repeat(consumerCount) { workId ->
        launch {
            for (job in jobChannel) {
                val result = Result(job.id, workId, isPrime(job.number))
                println("Consumer $workId processing job=$job, result=$result")
                delay(100L)
            }
        }
    }
}
