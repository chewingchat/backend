package org.chewing.v1.implementation.feed.like

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLikeHandler(
    private val feedLikeProcessor: FeedLikeProcessor
) {

    fun handleFeedLikes(feedId: String, userId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                feedLikeProcessor.processFeedLikes(feedId, userId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
        throw ConflictException(ErrorCode.FEED_LIKED_FAILED)
    }

    fun handleFeedUnLikes(feedId: String, userId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                feedLikeProcessor.processFeedUnLikes(feedId, userId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
        throw ConflictException(ErrorCode.FEED_UNLIKED_FAILED)
    }
}
