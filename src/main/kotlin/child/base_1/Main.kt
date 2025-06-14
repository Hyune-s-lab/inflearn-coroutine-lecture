package child.base_1

import kotlinx.coroutines.*

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main() {
    lateinit var globalJob: Job
    lateinit var customJob: Job

    try {
        runBlocking {
            log("parent - child 시작")

            val parentJob = launch {
                log("[A] 자식 코루틴 시작")
                delay(500L)
                throw RuntimeException("[A] 예외 발생")
            }

            globalJob = GlobalScope.launch {
                try {
                    log("[B] Global Scope 시작. 부모와 무관")
                    delay(2000L)
                    log("[B] Global Scope 완료")
                } catch (e: CancellationException) {
                    log("[B] 전파 취소 감지됨")
                }
            }

            customJob = launch {
                try {
                    log("[C] 새로운 Job 시작")
                    delay(2000L)
                    log("[C] 새로운 Job 완료")
                } catch (e: CancellationException) {
                    log("[C] 전파 취소 감지됨")
                }
            }

            delay(1500L)
        }
    } catch (e: Exception) {
        log("run ended")
    }

    runBlocking {
        globalJob.join()
        customJob.join()
        log("모든 작업 완료")
    }
}
