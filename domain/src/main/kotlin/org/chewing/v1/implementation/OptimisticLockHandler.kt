package org.chewing.v1.implementation

import kotlinx.coroutines.delay
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class OptimisticLockHandler {
    suspend fun <T> retryOnOptimisticLock(
        maxRetry: Int = 5,
        initialDelay: Long = 100L,
        maxDelay: Long = 800L,
        action: suspend () -> T,
    ): T? {
        var retryCount = 0
        var delayTime = initialDelay

        while (retryCount < maxRetry) {
            try {
                return action()
            } catch (ex: OptimisticLockingFailureException) {
                retryCount++
                delay(delayTime)
                delayTime = (delayTime * 2).coerceAtMost(maxDelay)
            }
        }
        return null
    }
}
