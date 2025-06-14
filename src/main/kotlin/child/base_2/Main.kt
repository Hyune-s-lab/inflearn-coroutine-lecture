package child.base_2

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext

suspend fun log(msg: String) = println("[${coroutineContext}] $msg")

fun main() = runBlocking<Unit> {
    log("start")

    launch(CoroutineName("CoroutineName")) {
        log("start")
        delay(500L)
        log("end")
    }

    launch(CoroutineName("CoroutineName2")) {
        log("start")
        delay(1000L)
        log("end")
    }
}
