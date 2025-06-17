package channel.base_2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun CoroutineScope.produceNumbers(): Channel<Int> {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            channel.send(x)
        }
        channel.close()
    }

    return channel
}

fun CoroutineScope.square(numbers: Channel<Int>): Channel<Int> {
    val channel = Channel<Int>()
    launch {
        for (x in numbers) {
            channel.send(x * x)
        }
        channel.close()
    }

    return channel
}

fun main() = runBlocking {
    val numbers = produceNumbers()
    val squares = square(numbers)

    for (sq in squares) {
        println(sq)
    }
}
