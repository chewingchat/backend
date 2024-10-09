package org.chewing.v1.implementation.feed.comment

import kotlinx.coroutines.*
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.IoScope
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class CommentLocker(
    private val commentHandler: CommentHandler,
    @IoScope private val ioScope: CoroutineScope
) {
    fun lockComment(userId: String, feedId: String, comment: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                commentHandler.handleComment(userId, feedId, comment, target)
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

    fun lockUnComment(commentId: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                commentHandler.handleUnComment(commentId, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
    }

    fun lockUnComments(commentIds: List<String>, target: FeedTarget) = runBlocking {
        val jobs = commentIds.map { commentId ->
            ioScope.launch {
                lockUnComment(commentId, target)
            }
        }
        jobs.forEach { it.join() }
    }
}