package org.chewing.v1.implementation.comment

import org.chewing.v1.model.feed.FeedTarget
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class CommentLocker(
    private val commentProcessor: CommentProcessor
) {
    fun lockComments(userId: String, feedId: String, comment: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processFeedComments(userId, feedId, comment, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockUnComments(commentId: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processFeedUnComments(commentId, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }
}