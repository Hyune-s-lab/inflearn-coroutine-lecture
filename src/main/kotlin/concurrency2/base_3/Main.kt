package concurrency2.base_3

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

sealed class AccountMsg
data class Deposit(val amount: Int) : AccountMsg()
data class Withdraw(val amount: Int, val response: CompletableDeferred<Boolean>) : AccountMsg()
data class GetBalance(val response: CompletableDeferred<Int>) : AccountMsg()
data class Transfer(
    val to: SendChannel<AccountMsg>,
    val amount: Int,
    val response: CompletableDeferred<Boolean>
) : AccountMsg()

fun CoroutineScope.accountActor(initialBalance: Int) = actor<AccountMsg> {
    var balance = initialBalance
    for (msg in channel) {
        when (msg) {
            is Deposit -> balance += msg.amount
            is Withdraw -> {
                if (balance >= msg.amount) {
                    balance -= msg.amount
                    msg.response.complete(true)
                } else {
                    msg.response.complete(false)
                }
            }

            is GetBalance -> msg.response.complete(balance)
            is Transfer -> {
                if (balance >= msg.amount) {
                    balance -= msg.amount
                    msg.to.send(Deposit(msg.amount))
                    msg.response.complete(true)
                } else {
                    msg.response.complete(false)
                }
            }
        }
    }
}

fun main() = runBlocking {
    val a = accountActor(1000)
    val b = accountActor(500)

    val jobs = mutableListOf<Job>()

    jobs += launch { a.send(Deposit(200)) }

    jobs += launch {
        val result = CompletableDeferred<Boolean>()
        a.send(Withdraw(300, result))
        println("A 출금(300) ${result.await()}")
    }

    jobs += launch {
        val result = CompletableDeferred<Boolean>()
        a.send(Transfer(b, 400, result))
        println("A->B 이체(400) ${result.await()}")
    }

    repeat(100) {
        jobs += launch {
            val result = CompletableDeferred<Boolean>()
            a.send(Withdraw(10, result))
            if (!result.await()) {
                println("A 출금 실패")
            } else {
                println("A 출금 성공")
            }
        }
    }

    jobs.forEach { it.join() }

    val aBalance = CompletableDeferred<Int>()
    a.send(GetBalance(aBalance))
    val bBalance = CompletableDeferred<Int>()
    b.send(GetBalance(bBalance))
    println("A 최종 잔고 ${aBalance.await()}")
    println("B 최종 잔고 ${bBalance.await()}")
}
