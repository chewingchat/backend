package org.chewing.v1.service

import org.chewing.v1.implementation.feed.*
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.FriendFeed
import org.chewing.v1.model.User
import org.springframework.stereotype.Service
import java.io.File

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedChecker: FeedChecker,
    private val feedLocker: FeedLocker,
    private val feedRemover: FeedRemover,
    private val feedValidator: FeedValidator,
    private val fileProcessor: FileProcessor,
    private val feedProcessor: FeedProcessor,
) {
    fun getFriendFeed(userId: User.UserId, feedId: Feed.FeedId): FriendFeed {
        val feed = feedReader.readFulledFeed(feedId)
        val isLiked = feedChecker.checkFeedLike(feedId, userId)
        return FriendFeed.of(feed, isLiked)
    }

    fun getFeed(userId: User.UserId, feedId: Feed.FeedId): Feed {
        return feedReader.readFulledFeed(feedId)
    }

    fun getFriendFulledFeeds(userId: User.UserId): List<FriendFeed> {
        val feeds = feedReader.readFulledFeedsByUserId(userId)
        val likedFeedIds = feedChecker.checkFeedsLike(feeds.map { it.feedId }, userId)
        return feeds.map { feed ->
            val isLiked = likedFeedIds[feed.feedId] ?: false
            FriendFeed.of(feed, isLiked)
        }
    }
    fun getUserFulledFeeds(userId: User.UserId): List<Feed> {
        return feedReader.readFulledFeedsByUserId(userId)
    }

    fun addFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedValidator.isAlreadyLiked(feedId, userId)
        feedLocker.lockFeedLikes(feedId, userId)
    }

    fun deleteFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedValidator.isAlreadyUnliked(feedId, userId)
        feedLocker.lockFeedUnLikes(feedId, userId)
    }

    fun deleteFeeds(userId: User.UserId, feedIds: List<Feed.FeedId>) {
        val feeds = feedValidator.isFeedsOwner(feedIds, userId)
        feedRemover.removeFeeds(feedIds)
        val medias = feeds.flatMap { feed -> feed.feedDetails.map { it.media } }
        fileProcessor.processPreFiles(medias)
    }

    fun createFeed(userId: User.UserId, files: List<File>, topic: String) {
        val medias = fileProcessor.processNewFiles(userId, files)
        feedProcessor.processNewFeed(medias, userId, topic)
    }
}