package org.chewing.v1.implementation.feed.feed

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.repository.feed.FeedDetailRepository
import org.chewing.v1.repository.feed.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedReader(
    private val feedRepository: FeedRepository,
    private val feedDetailRepository: FeedDetailRepository
) {
    fun readInfo(feedId: String): FeedInfo {
        return feedRepository.read(feedId) ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }

    fun readsOwnedInfo(userId: String, feedStatus: FeedStatus): List<FeedInfo> {
        return feedRepository.readsOwned(userId, feedStatus)
    }

    fun readsInfo(feedIds: List<String>): List<FeedInfo> {
        return feedRepository.reads(feedIds)
    }

    fun readDetails(feedId: String): List<FeedDetail> {
        return feedDetailRepository.read(feedId)
    }

    fun readsMainDetails(feedIds: List<String>): List<FeedDetail> {
        return feedDetailRepository.readsFirstIndex(feedIds)
    }
}
