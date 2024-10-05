package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.repository.FeedLikesRepository
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.NotFound

@Component
class FeedValidator(
    private val feedReader: FeedReader,
    private val feedLikesRepository: FeedLikesRepository
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

    fun isNotOwned(feedId: String, userId: String) {
        val feedInfo = feedReader.readInfo(feedId)
        if(feedInfo.userId == userId) {
            throw ConflictException(ErrorCode.FEED_IS_OWNED)
        }
    }

    fun isAlreadyLiked(feedId: String, userId: String) {
        if (feedLikesRepository.checkLike(feedId, userId)) {
            throw ConflictException(ErrorCode.FEED_ALREADY_LIKED)
        }
    }

    fun isAlreadyUnliked(feedId: String, userId: String) {
        if (!feedLikesRepository.checkLike(feedId, userId)) {
            throw ConflictException(ErrorCode.FEED_ALREADY_UNLIKED)
        }
    }
}