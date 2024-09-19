package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.feed.FulledFeed
import org.chewing.v1.model.feed.UserFeed
import org.chewing.v1.repository.FeedRepository
import org.chewing.v1.repository.FriendRepository
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class FeedReader(
    private val feedRepository: FeedRepository,
) {
    fun readFulledFeed(feedId: Feed.FeedId): FulledFeed {
        val feed = feedRepository.readFeed(feedId) ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
        val feedDetails = feedRepository.readFeedDetails(feedId)
        return FulledFeed.of(feed, feedDetails)
    }

    fun readFeed(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFeed(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }

    fun readFulledFeedsByUserId(userId: User.UserId): List<FulledFeed> {
        val feeds = feedRepository.readFeedByUserId(userId)
        val feedDetailsInfo = feedRepository.readFeedsDetails(feeds.map { it.id })
        return feeds.map { feed -> FulledFeed.of(feed, feedDetailsInfo[feed.id] ?: emptyList()) }
    }
    fun readFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean> {
        return feedRepository.readFeedsLike(feedIds, userId)
    }

    fun readFeedByCommentId(commentId: Comment.CommentId): Feed {
        val feed = feedRepository.readFeedByCommentId(commentId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }

    fun readFulledFeedsByOwner(
        feedIds: List<Feed.FeedId>,
        friendIds: List<User.UserId>
    ): List<Pair<FulledFeed, User.UserId>> {
        val feedWithUserId = feedRepository.readFeedsByOwner(feedIds, friendIds)
        val feedDetailsInfo = feedRepository.readFeedsDetails(feedWithUserId.map { it.first.id })
        return feedWithUserId.map { (feed, userId) ->
            Pair(
                FulledFeed.of(feed, feedDetailsInfo[feed.id] ?: emptyList()),
                userId
            )
        }
    }
}