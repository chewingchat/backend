package org.chewing.v1.implementation.feed

import org.chewing.v1.model.feed.FeedTarget
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLocker(
    private val feedProcessor: FeedProcessor
) {
    fun lockFeedLikes(feedId: String, userId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedLikes(feedId, userId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedUnLikes(feedId: String, userId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedUnLikes(feedId, userId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }
}