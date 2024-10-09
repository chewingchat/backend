package org.chewing.v1.implementation.feed.feed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.IoScope
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class FeedLocker(
    private val feedUpdater: FeedUpdater,
    @IoScope private val ioScope: CoroutineScope
) {

    fun lockFeedHide(feedId: String, updateType: FeedTarget) {
        var retryCount = 0
        val maxRetry = 10
        while (retryCount < maxRetry) {
            try {
                feedUpdater.update(feedId, updateType)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(1000)
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