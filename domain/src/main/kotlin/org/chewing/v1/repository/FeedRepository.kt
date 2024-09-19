package org.chewing.v1.repository

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository {
    fun readFeed(feedId: Feed.FeedId): Feed?
    fun readFeeds(feedIds: List<Feed.FeedId>): List<Feed>
    fun readFeedByUserId(userId: User.UserId): List<Feed>
    fun readFeedByCommentId(commentId: Comment.CommentId): Feed?
    fun readFeedDetails(feedId: Feed.FeedId): List<FeedDetail>
    fun readFeedsDetails(feedIds: List<Feed.FeedId>): Map<Feed.FeedId, List<FeedDetail>>
    fun readFeedsByOwner(feedIds: List<Feed.FeedId>,userIds:List<User.UserId>): List<Pair<Feed, User.UserId>>
    fun isAlreadyLiked(feedId: Feed.FeedId, userId: User.UserId): Boolean
    fun readFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean>
    fun isFeedOwner(feedId: Feed.FeedId, userId: User.UserId): Boolean
    fun isFeedsOwner(feedIds: List<Feed.FeedId>, userId: User.UserId): Boolean
    fun appendFeedLikes(feed: Feed, user: User)
    fun removeFeedLikes(feed: Feed, user: User)
    fun removeFeeds(feedIds: List<Feed.FeedId>)
    fun removeFeedsDetail(feedIds: List<Feed.FeedId>): List<Media>
    fun appendFeed(medias: List<Media>, user: User, topic: String): Feed.FeedId
    fun updateFeed(feed: Feed, target: FeedTarget)
}