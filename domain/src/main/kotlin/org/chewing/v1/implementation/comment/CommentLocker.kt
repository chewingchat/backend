package org.chewing.v1.implementation.comment

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.IoScope
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class CommentLocker(
    private val commentProcessor: CommentProcessor,
    @IoScope private val ioScope: CoroutineScope
) {
    fun lockComment(userId: String, feedId: String, comment: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processComment(userId, feedId, comment, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockUnComment(commentId: String, target: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                commentProcessor.processUnComment(commentId, target)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockUnComments(commentIds: List<String>, target: FeedTarget) {
        commentIds.forEach { commentId ->
            ioScope.launch {
                lockUnComment(commentId, target)
            }
        }
    }

}