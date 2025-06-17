package channel.base_5

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()

    repeat(3) { id ->
        launch {
            for (msg in channel) {
                println("worker $id get $msg")
            }
        }
    }

    launch {
        repeat(9) { channel.send(it) }
        channel.close()
    }
}

