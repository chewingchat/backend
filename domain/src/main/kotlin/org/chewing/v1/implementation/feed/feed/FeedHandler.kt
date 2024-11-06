package org.chewing.v1.implementation.feed.feed

import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedHandler(
    private val feedUpdater: FeedUpdater,
    private val asyncJobExecutor: AsyncJobExecutor
) {

    fun lockFeedHide(feedId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                feedUpdater.update(feedId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
    }

    fun lockFeedHides(feedIds: List<String>, updateType: FeedTarget) {
        asyncJobExecutor.executeAsyncJobs(feedIds) { feedId ->
            lockFeedHide(feedId, updateType)
        }
    }
}
