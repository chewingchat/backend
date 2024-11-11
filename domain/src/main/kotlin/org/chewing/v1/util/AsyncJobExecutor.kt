package org.chewing.v1.util

import kotlinx.coroutines.*
import org.springframework.stereotype.Component

@Component
class AsyncJobExecutor(
    @IoScope private val ioScope: CoroutineScope,
) {

    fun <T> executeAsyncJobs(
        items: List<T>,
        action: suspend (T) -> Unit,
    ) = runBlocking {
        val jobs = items.map { item ->
            ioScope.async {
                action(item)
            }
        }
        jobs.awaitAll()
    }

    fun <T> executeAsyncJob(
        item: T,
        action: suspend (T) -> Unit,
    ) = runBlocking {
        val job = ioScope.async {
            action(item)
        }
        job.await()
    }

    fun <T> executeAsyncJobsTest(
        items: List<T>,
        action: suspend (T) -> Unit,
    ) = runBlocking {
        val jobs = items.map { item ->
            ioScope.launch {
                // 병렬 실행이 아닌 순차 비동기 작업
                action(item)
            }
        }
        jobs.forEach { it.join() } // 모든 작업이 끝날 때까지 기다림
    }
}
