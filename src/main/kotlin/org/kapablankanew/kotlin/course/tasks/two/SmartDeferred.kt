package org.kapablankanew.kotlin.course.tasks.two

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class SmartDeferred<T> (
    private val deferred: Deferred<T>,
    private val dependencies: List<Deferred<*>>
) {
    suspend fun await(startLazy: Boolean = false, coroutineContext: CoroutineContext = EmptyCoroutineContext): T {
        if (!startLazy) {
            CoroutineScope(coroutineContext).launch {
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