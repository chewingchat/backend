package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedReader(
    private val feedRepository: FeedRepository,
) {
    fun readFeedWithDetails(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFeedWithDetails(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }

    fun readFeed(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFeed(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }
    fun readFeedWithWriter(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFeedWithWriter(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }
    fun readFeedsWithDetails(userId: User.UserId): List<Feed> {
        return feedRepository.readFeedsWithDetails(userId)
    }
    fun readFeedComment(feedId: Feed.FeedId): List<FeedComment> {
        return feedRepository.readFeedComment(feedId)
    }

    fun readFeedComments(commentIds: List<FeedComment.CommentId>): List<FeedComment> {
        return feedRepository.readFeedComments(commentIds)
    }
}