package concurrency2.base_1

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

data class Account(
    val id: Int,
    var balance: Int,
    val mutex: Mutex = Mutex()
)

suspend fun transfer(from: Account, to: Account, amount: Int) {
    val first = if (from.id < to.id) from else to
    val second = if (from.id < to.id) to else from

    first.mutex.withLock {
        second.mutex.withLock {
            if (from.balance >= amount) {
                from.balance -= amount
                to.balance += amount
                println("Transferred $amount from ${from.id} to ${to.id}. New balances: ${from.balance}, ${to.balance}")
            }
        }
    }
}

fun main() = runBlocking {
    val accounts = List(3) { Account(it, 1000) }

    val jobs = List(100) {
        launch {
            repeat(100) {
                val fromIndex = Random.nextInt(accounts.size)
                var toIndex = Random.nextInt(accounts.size)

                while (toIndex == fromIndex) {
                    toIndex = Random.nextInt(accounts.size)
                }

                val amount = Random.nextInt(1, 30)
                transfer(accounts[fromIndex], accounts[toIndex], amount)
            }
        }
    }

    jobs.forEach { it.join() }

    val total = accounts.sumOf { it.balance }
    println("total balance: $total")
}
