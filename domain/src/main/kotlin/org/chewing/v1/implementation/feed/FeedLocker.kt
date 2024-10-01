package org.chewing.v1.implementation.feed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.IoScope
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLocker(
    private val feedProcessor: FeedProcessor,
    @IoScope private val ioScope: CoroutineScope
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

    fun lockFeedHide(feedId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedHides(feedId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedUnHide(feedId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedProcessor.processFeedUnHides(feedId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
            }
        }
    }

    fun lockFeedUnHides(feedIds: List<String>, updateType: FeedTarget) {
        feedIds.forEach { feedId ->
            ioScope.launch {
                lockFeedUnHide(feedId, updateType)
            }
        }
    }

    fun lockFeedHides(feedIds: List<String>, updateType: FeedTarget) {
        feedIds.forEach { feedId ->
            ioScope.launch {
                lockFeedHide(feedId, updateType)
            }
        }
    }
}