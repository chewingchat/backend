package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedValidator(
    private val feedRepository: FeedRepository
) {
    fun isOwner(feedId: String, userId: String) {
        if(!feedRepository.isOwner(feedId, userId)){
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }

    fun isFeedsOwner(feedIds: List<String>, userId: String) {
        if (feedRepository.isAllOwner(feedIds, userId)) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }

    fun isNotOwner(feedId: String, userId: String) {
        if(feedRepository.isOwner(feedId, userId)){
            throw ConflictException(ErrorCode.FEED_IS_OWNED)
        }
    }

    fun isAlreadyLiked(feedId: String, userId: String) {
        if (feedRepository.isAlreadyLiked(feedId, userId)) {
            throw ConflictException(ErrorCode.FEED_ALREADY_LIKED)
        }
    }

    fun isAlreadyUnliked(feedId: String, userId: String) {
        if (!feedRepository.isAlreadyLiked(feedId, userId)) {
            throw ConflictException(ErrorCode.FEED_ALREADY_UNLIKED)
        }
    }
}