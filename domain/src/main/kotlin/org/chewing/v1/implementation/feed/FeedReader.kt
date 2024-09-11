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
    fun readFulledFeed(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFulledFeed(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }
    fun readFeed(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFeed(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }
    fun readFulledFeeds(feedIds: List<Feed.FeedId>): List<Feed> {
        return feedRepository.readFulledFeeds(feedIds)
    }
    fun readFulledFeedsByUserId(userId: User.UserId): List<Feed> {
        return feedRepository.readFulledFeedsByUserId(userId)
    }
    fun readFulledFeedByCommentId(commentId: FeedComment.CommentId): Feed {
        return feedRepository.readFulledFeedByCommentId(commentId)
    }
}