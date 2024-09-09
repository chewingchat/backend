package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLocker(
    private val feedProcessor: FeedProcessor
) {
    fun lockFeedLikes(feedId: Feed.FeedId, userId: User.UserId) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedLikes(feedId, userId)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedUnLikes(feedId: Feed.FeedId, userId: User.UserId) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedUnLikes(feedId, userId)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedComments(userId: User.UserId, feedId: Feed.FeedId, comment: String) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedComments(userId, feedId, comment)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedUnComments(feedId: Feed.FeedId, commentId: FeedComment.CommentId) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedUnComments(feedId, commentId)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }
}