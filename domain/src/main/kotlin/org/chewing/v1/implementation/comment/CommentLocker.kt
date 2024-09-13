package org.chewing.v1.implementation.comment

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class CommentLocker(
    private val commentProcessor: CommentProcessor
) {
    fun lockFeedComments(userId: User.UserId, feedId: Feed.FeedId, comment: String) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processFeedComments(userId, feedId, comment)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedUnComments(commentId: FeedComment.CommentId) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processFeedUnComments(commentId)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }
}