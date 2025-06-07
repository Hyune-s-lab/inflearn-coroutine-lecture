package base.base_2

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlin.random.Random

//fun main() = runBlocking {
//    val job = launch {
//        delay(1000L)
//        println("world")
//    }
//
//    println("hello")
//    job.join()  // Wait for the coroutine to finish
//    println("done")
//}

//fun main() = runBlocking {
//    doWork()
//}
//
//suspend fun doWork() = coroutineScope {
//    launch {
//        delay(1000L)
//        println("world")
//    }
//
//    println("hello")
//}

fun main() = runBlocking {
    val mock = listOf(1, 2, 3, 4, 5)
    try {
        val result = supervisorScope {
            mock.map {
                async {
                    fetch(it)
                }
            }.mapNotNull { deferred ->
                try {
                    deferred.await()
                } catch (e: Exception) {
                    // todo 자원 반환
                    println("Error fetching: ${e.message}")
                    null
                }
            }
        }
        println(result)
    } catch (e: Exception) {
        println(e.message)
    }
}

suspend fun fetch(id: Int): String {
    delay(Random.nextLong(500, 1500))
    if (Random.nextBoolean()) throw Exception("failed to fetch id: $id")
    return "fetched id: $id"
}
