package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedValidator(
    private val feedReader: FeedReader,
    private val userReader: UserReader,
    private val feedRepository: FeedRepository
) {
    fun isFeedOwner(feedId: Feed.FeedId, userId: User.UserId) {
        val feed = feedReader.readFeed(feedId)
        val user = userReader.readUser(userId)
        if (feed.writer.userId != user.userId) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }

    fun isNotFeedOwner(feedId: Feed.FeedId, userId: User.UserId) {
        val feed = feedReader.readFeed(feedId)
        val user = userReader.readUser(userId)
        if (feed.writer.userId == user.userId) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
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