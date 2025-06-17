package channel.base_1

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 1. Rendezvous Channel **
 * 2. Buffered Channel **
 * 3. Unlimited Channel
 * 4. Conflated Channel
 */

fun main() = runBlocking<Unit> {
    val bufferedChannel = Channel<Int>(10)
    val unlimitedChannel = Channel<Int>(Channel.UNLIMITED)
    val conflatedChannel = Channel<Int>(Channel.CONFLATED)

    val channel = Channel<Int>()

    launch {
        for (i in 1..5) {
            println("sending $i")
            channel.send(i)
            delay(100L)
        }
        channel.close()
    }

    launch {
        for (v in channel) {
            println("received $v")
            delay(200L)
        }
    }
}
