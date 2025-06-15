package flow.base_1

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

suspend fun main() {
    val simpleFlow = flow {
        emit(1)
        delay(100L)
        emit(2)
        delay(100L)
        emit(3)
    }

    /**
     * 1. publisher: 데이터 생성자
     * 2. subscriber: 데이터 소비자
     * 3. processor: 중간 단계 연결자
     */

    val flow = flow {
        println("start")
        emit(1)
    }

    println("flow 정의")

    flow.collect { value -> println("수신: $value") }
    flow.collect { value -> println("수신: $value") }
}
