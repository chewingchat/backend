package org.chewing.v1.implementation.feed.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.springframework.stereotype.Component

@Component
class FeedValidator(
    private val feedReader: FeedReader,
) {
    fun isOwned(feedId: String, userId: String) {
        val feedInfo = feedReader.readInfo(feedId)
        if (feedInfo.userId != userId) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }

    fun isFeedsOwner(feedIds: List<String>, userId: String) {
        val feedInfos = feedReader.readsInfo(feedIds)
        if (feedInfos.size != feedIds.size) {
            throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
        }
        if (feedInfos.any { it.userId != userId }) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }
}