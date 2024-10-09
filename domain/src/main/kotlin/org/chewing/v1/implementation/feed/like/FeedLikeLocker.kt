package org.chewing.v1.implementation.feed.like

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLikeLocker(
    private val feedLikeHandler: FeedLikeHandler
) {


    fun lockFeedLikes(feedId: String, userId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                feedLikeHandler.handleFeedLikes(feedId, userId, updateType)
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

    fun lockFeedUnLikes(feedId: String, userId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                feedLikeHandler.handleFeedUnLikes(feedId, userId, updateType)
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