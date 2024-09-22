package org.chewing.v1.service

import org.chewing.v1.implementation.feed.*
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.feed.*
import org.springframework.stereotype.Service
import java.io.File

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedLocker: FeedLocker,
    private val feedRemover: FeedRemover,
    private val feedValidator: FeedValidator,
    private val fileProcessor: FileProcessor,
    private val feedProcessor: FeedProcessor,
    private val feedEnricher: FeedEnricher,
) {
    // 피드를 가져옴
    fun getFeed(userId: String, feedId: String): Feed {
        val feed = feedReader.readFeed(feedId)
        val feedDetails = feedReader.readFeedDetails(feedId)

        return Feed.of(feed, feedDetails)
    }

    fun getFeeds(feedsId: List<String>): List<Feed> {
        val feeds = feedReader.reads(feedsId)
        val feedsDetails = feedReader.readsDetails(feedsId)
        return feedEnricher.enrichFeeds(feeds, feedsDetails)
    }

    //좋아요 유무가 포함되어야 함
    fun getFriendFeeds(userId: String, friendId: String): List<FriendFeed> {
        val feeds = feedReader.readsByUser(friendId)
        val feedsDetail = feedReader.readsDetails(feeds.map { it.feedId })
        val likedFeedIds = feedReader.readsLike(feeds.map { it.feedId }, userId)
        val friendFeeds = feedEnricher.enrichFriendFeeds(feeds, likedFeedIds, feedsDetail)
        return FeedSortEngine.sort(friendFeeds, SortCriteria.DATE)
    }

    fun likes(userId: String, feedId: String, target: FeedTarget) {
        feedValidator.isAlreadyLiked(feedId, userId)
        feedLocker.lockFeedLikes(feedId, userId, target)
    }

    fun unlikes(userId: String, feedId: String, target: FeedTarget) {
        feedValidator.isAlreadyUnliked(feedId, userId)
        feedLocker.lockFeedUnLikes(feedId, userId, target)
    }

    fun removes(userId: String, feedIds: List<String>) {
        feedValidator.isFeedsOwner(feedIds, userId)
        val oldMedias = feedRemover.removes(feedIds)
        fileProcessor.processOldFiles(oldMedias)
    }

    fun make(userId: String, files: List<File>, topic: String) {
        val medias = fileProcessor.processNewFiles(userId, files)
        feedProcessor.processNewFeed(medias, userId, topic)
    }
}