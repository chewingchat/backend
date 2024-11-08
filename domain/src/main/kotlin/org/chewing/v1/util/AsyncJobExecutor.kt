package org.chewing.v1.util

import kotlinx.coroutines.*
import org.springframework.stereotype.Component

@Component
class AsyncJobExecutor(
    @IoScope private val ioScope: CoroutineScope
) {

    fun <T> executeAsyncJobs(
        items: List<T>, action: suspend (T) -> Unit
    ) = runBlocking {
        val jobs = items.map { item ->
            ioScope.async {
                action(item)
            }
        }
        jobs.awaitAll()
    }
}