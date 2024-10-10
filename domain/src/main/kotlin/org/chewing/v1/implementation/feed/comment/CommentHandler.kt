package org.chewing.v1.implementation.feed.comment

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class CommentHandler(
    private val commentProcessor: CommentProcessor,
    private val asyncJobExecutor: AsyncJobExecutor
) {
    fun handleComment(userId: String, feedId: String, comment: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processComment(userId, feedId, comment, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
        throw ConflictException(ErrorCode.FEED_COMMENT_FAILED)
    }

    fun handleUnComment(commentId: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processUnComment(commentId, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
    }

    fun handleUnComments(commentIds: List<String>, target: FeedTarget) {
        asyncJobExecutor.executeAsyncJobs(commentIds) { commentId ->
            handleUnComment(commentId, target)
        }
    }
}