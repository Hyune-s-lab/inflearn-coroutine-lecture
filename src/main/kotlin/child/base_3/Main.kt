package child.base_3

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MyActive : CoroutineScope {
    private val activeJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = activeJob + Dispatchers.Default

    fun startOperation() {
        launch {
            println("[${Thread.currentThread().name}] 데이터 패치 시작")
            delay(1000L)
            println("[${Thread.currentThread().name}] 데이터 패치 완료")
        }

        launch {
            println("[${Thread.currentThread().name}] 애니메이션 시작")
            delay(3000L)
            println("[${Thread.currentThread().name}] 애니메이션 종료")
        }
    }

    fun destroy() {
        println("Coroutine scope destroyed")
        activeJob.cancel()
    }
}

fun main() = runBlocking {
    println("예제 시작")

    val active = MyActive()
    active.startOperation()

    delay(4500L)
    active.destroy()

    println("예제 종료")
}
