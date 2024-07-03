package org.kapablankanew.kotlin.course.tasks.two

import kotlinx.coroutines.*

class SmartDeferred<T> (
    private val deferred: Deferred<T>,
    private val dependencies: List<Deferred<*>>
) {
    suspend fun await(startLazy: Boolean = false): T {
        if (!startLazy) {
            coroutineScope {
                dependencies.forEach {
                    it.start()
                }
            }
        }
        return deferred.await()
    }
}

fun <T> Deferred<T>.toSmartDeferred(dependencies: List<Deferred<*>>): SmartDeferred<T> {
    return SmartDeferred(this, dependencies)
}