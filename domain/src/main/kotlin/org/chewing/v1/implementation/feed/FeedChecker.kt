package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedChecker(
    private val feedRepository: FeedRepository
) {
    fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean {
        return feedRepository.checkFeedLike(feedId, userId)
    }

    fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean> {
        return feedRepository.checkFeedsLike(feedIds, userId)
    }

    fun isAlreadyLiked(feedId: Feed.FeedId, userId: User.UserId) {
        if(feedRepository.checkFeedLike(feedId, userId)){
            throw ConflictException(ErrorCode.FEED_ALREADY_LIKED)
        }
    }

    fun isAlreadyUnliked(feedId: Feed.FeedId, userId: User.UserId) {
        if(!feedRepository.checkFeedLike(feedId, userId)){
            throw ConflictException(ErrorCode.FEED_ALREADY_UNLIKED)
        }
    }
}