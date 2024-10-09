package org.chewing.v1.service

import org.chewing.v1.implementation.feed.feed.*
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.model.feed.*
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedLocker: FeedLocker,
    private val feedAppender: FeedAppender,
    private val feedValidator: FeedValidator,
    private val fileProcessor: FileProcessor,
    private val feedEnricher: FeedEnricher,
    private val feedRemover: FeedRemover
) {
    // 피드를 가져옴
    fun getOwnedFeed(feedId: String, type: FeedStatus): Feed {
        val feed = feedReader.readInfo(feedId)
        val feedDetails = feedReader.readDetails(feedId)
        return Feed.of(feed, feedDetails)
    }

    fun getFeeds(feedsId: List<String>): List<Feed> {
        val feeds = feedReader.readsInfo(feedsId)
        val feedsDetails = feedReader.readsMainDetails(feedsId)
        return feedEnricher.enrichFeeds(feeds, feedsDetails)
    }

    fun getOwnedFeeds(targetUserId: String, feedStatus: FeedStatus): List<Feed> {
        val feeds = feedReader.readsOwnedInfo(targetUserId, feedStatus)
        val feedsDetail = feedReader.readsMainDetails(feeds.map { it.feedId })
        return feedEnricher.enrichFeeds(feeds, feedsDetail)
    }

    fun removes(userId: String, feedIds: List<String>) {
        feedValidator.isFeedsOwner(feedIds, userId)
        val oldMedias = feedRemover.removes(feedIds)
        fileProcessor.processOldFiles(oldMedias)
    }

    fun changeHide(userId: String, feedIds: List<String>, target: FeedTarget) {
        feedValidator.isFeedsOwner(feedIds, userId)
        feedLocker.lockFeedHides(feedIds, target)
    }


    fun make(userId: String, files: List<FileData>, topic: String, category: FileCategory) {
        val medias = fileProcessor.processNewFiles(userId, files, category)
        feedAppender.appendFeed(medias, userId, topic)
    }
}