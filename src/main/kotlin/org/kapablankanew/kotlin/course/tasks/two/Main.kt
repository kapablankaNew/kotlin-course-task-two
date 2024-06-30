package org.kapablankanew.kotlin.course.tasks.two

import kotlinx.coroutines.*
import java.time.Instant
import kotlin.system.measureTimeMillis
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun main() {
    test(startLazy = false)
    test(startLazy = true)
}

fun test(startLazy: Boolean) = runBlocking {
    val intOne = async(start = CoroutineStart.LAZY) { getFirstInt() }
    val intTwo = async(start = CoroutineStart.LAZY) { getSecondInt() }
    val intThree = async(start = CoroutineStart.LAZY) { getThirdInt() }
    val deps = listOf(intOne, intTwo, intThree)
    val message = async(start = CoroutineStart.LAZY) { getMessage(deps) }.toSmartDeferred(deps)
    val time = measureTimeMillis {
        message.await(startLazy = startLazy)
    }
    println("Time of test with lazy = $startLazy: ${time.toDuration(DurationUnit.MILLISECONDS)} seconds")
}

suspend fun getMessage(members: List<Deferred<Int>>): Int {
    println("Start get message, ${Instant.now()}")
    var result = 0
    members.forEach {
        result += it.await()
    }
    println("Finish get message, ${Instant.now()}")
    return result
}

suspend fun getFirstInt(): Int {
    println("Start get first, ${Instant.now()}")
    delay(3000L)
    println("Finish get first, ${Instant.now()}")
    return 1
}

suspend fun getSecondInt(): Int {
    println("Start get second, ${Instant.now()}")
    delay(5000L)
    println("Finish get second, ${Instant.now()}")
    return 2
}

suspend fun getThirdInt(): Int {
    println("Start get third, ${Instant.now()}")
    delay(10000L)
    println("Finish get third, ${Instant.now()}")
    return 3
}