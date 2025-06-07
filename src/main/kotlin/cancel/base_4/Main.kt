package cancel.base_4

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun main() = runBlocking {
    val file = File("test.txt")
    file.writeText("hello world")
    val reader = BufferedReader(FileReader(file))

    val job = launch {
        try {
            repeat(1000) {
                val line = reader.readLine()
                if (line == null) return@repeat
                println("Read line: $line")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("clean up")
                reader.close()
                println("clean up end")
            }
        }
    }

    delay(1300L)
    println("job: I'm tired of waiting!")

    job.cancelAndJoin()
    println("done")
}
