package channel.base_4

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val channel = Channel<String>()
    val producerCount = 3

    repeat(producerCount) { idx ->
        launch {
            repeat(5) { msg ->
                channel.send("Producer $idx: Message $msg")
                delay(100L + idx * 50)
            }
        }
    }

    for (msg in channel) {
        println("Received: $msg")
        delay(200L)
    }
}
