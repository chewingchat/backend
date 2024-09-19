package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedValidator(
    private val feedRepository: FeedRepository
) {
    fun isFeedOwner(feedId: Feed.FeedId, userId: User.UserId) {
        if(!feedRepository.isFeedOwner(feedId, userId)){
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }

    fun isFeedsOwner(feedIds: List<Feed.FeedId>, userId: User.UserId) {
        if (feedRepository.isFeedsOwner(feedIds, userId)) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }

    fun isNotFeedOwner(feedId: Feed.FeedId, userId: User.UserId) {
        if(feedRepository.isFeedOwner(feedId, userId)){
            throw ConflictException(ErrorCode.FEED_IS_OWNED)
        }
    }

    fun isAlreadyLiked(feedId: Feed.FeedId, userId: User.UserId) {
        if (feedRepository.isAlreadyLiked(feedId, userId)) {
            throw ConflictException(ErrorCode.FEED_ALREADY_LIKED)
        }
    }

    fun isAlreadyUnliked(feedId: Feed.FeedId, userId: User.UserId) {
        if (!feedRepository.isAlreadyLiked(feedId, userId)) {
            throw ConflictException(ErrorCode.FEED_ALREADY_UNLIKED)
        }
    }
}