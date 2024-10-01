package org.chewing.v1.service

import org.chewing.v1.implementation.feed.*
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.feed.*
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedLocker: FeedLocker,
    private val feedRemover: FeedRemover,
    private val feedValidator: FeedValidator,
    private val fileProcessor: FileProcessor,
    private val feedProcessor: FeedProcessor,
    private val feedEnricher: FeedEnricher,
    private val feedChecker: FeedChecker
) {
    // 피드를 가져옴
    fun getFeed(userId: String, feedId: String, type: FeedOwner): Pair<Feed, Boolean> {
        val feed = feedReader.readFeed(feedId)
        val feedDetails = feedReader.readFeedDetails(feedId)
        val isLiked = feedChecker.checkLike(feedId, userId)
        return Pair(Feed.of(feed, feedDetails), isLiked)
    }

    fun getFeeds(feedsId: List<String>): List<Feed> {
        val feeds = feedReader.reads(feedsId)
        val feedsDetails = feedReader.readsDetails(feedsId)
        return feedEnricher.enrichFeeds(feeds, feedsDetails)
    }

    fun getFeeds(targetUserId: String, feedOwner: FeedOwner): List<Feed> {
        val feeds = feedReader.readsByUserId(targetUserId, feedOwner)
        val feedsDetail = feedReader.readsDetails(feeds.map { it.feedId })
        val enrichedFeeds = feedEnricher.enrichFeeds(feeds, feedsDetail)
        return FeedSortEngine.sort(enrichedFeeds, SortCriteria.DATE)
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



    fun hides(userId: String, feedIds: List<String>, target: FeedTarget) {
        feedValidator.isFeedsOwner(feedIds, userId)
        feedLocker.lockFeedHides(feedIds, target)
    }

    fun unHides(userId: String, feedIds: List<String>, target: FeedTarget) {
        feedValidator.isFeedsOwner(feedIds, userId)
        feedLocker.lockFeedUnHides(feedIds, target)
    }

    fun make(userId: String, files: List<FileData>, topic: String, category: FileCategory) {
        val medias = fileProcessor.processNewFiles(userId, files, category)
        feedProcessor.processNewFeed(medias, userId, topic)
    }
}