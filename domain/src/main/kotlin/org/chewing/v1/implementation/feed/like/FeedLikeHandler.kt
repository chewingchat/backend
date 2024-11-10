package org.chewing.v1.implementation.feed.like

import kotlinx.coroutines.delay
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLikeHandler(
    private val feedLikeProcessor: FeedLikeProcessor,
    private val asyncJobExecutor: AsyncJobExecutor,
) {

    suspend fun executeFeedLikes(feedId: String, userId: String, updateType: FeedTarget) {
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
                delay(delayTime)
                delayTime *= 2
            }
        }
        throw ConflictException(ErrorCode.FEED_LIKED_FAILED)
    }

    suspend fun executeFeedUnLikes(feedId: String, userId: String, updateType: FeedTarget) {
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
                delay(delayTime)
                delayTime *= 2
            }
        }
        throw ConflictException(ErrorCode.FEED_UNLIKED_FAILED)
    }

    fun handleFeedLikes(feedId: String, userId: String, updateType: FeedTarget) {
        asyncJobExecutor.executeAsyncJob(userId) {
            executeFeedLikes(feedId, userId, updateType)
        }
    }

    fun handleFeedUnLikes(feedId: String, userId: String, updateType: FeedTarget) {
        asyncJobExecutor.executeAsyncJob(userId) {
            executeFeedUnLikes(feedId, userId, updateType)
        }
    }
}
