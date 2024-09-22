package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedReader(
    private val feedRepository: FeedRepository,
) {
    fun readFeed(feedId: String): FeedInfo {
        val feed = feedRepository.read(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }

    fun readsLike(feedIds: List<String>, userId: String): List<String> {
        return feedRepository.readsLike(feedIds, userId)
    }

    fun readsByUser(userId: String): List<FeedInfo> {
        return feedRepository.readsByUserId(userId)
    }

    fun reads(feedIds: List<String>): List<FeedInfo> {
        return feedRepository.reads(feedIds)
    }

    fun readFeedDetails(feedId: String): List<FeedDetail> {
        return feedRepository.readDetails(feedId)
    }

    fun readsDetails(feedIds: List<String>): List<FeedDetail> {
        return feedRepository.readsDetails(feedIds)
    }
}