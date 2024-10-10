package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedDetailJpaEntity
import org.chewing.v1.jparepository.FeedDetailJpaRepository
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
internal class FeedDetailRepositoryImpl(
    private val feedDetailJpaRepository: FeedDetailJpaRepository
) : FeedDetailRepository {
    override fun read(feedId: String): List<FeedDetail> {
        return feedDetailJpaRepository.findAllByFeedIdOrderByFeedIndex(feedId).map { it.toFeedDetail() }
    }


    override fun readsFirstIndex(feedIds: List<String>): List<FeedDetail> {
        val feedDetails = feedDetailJpaRepository.findByFeedIdInAndFeedIndex(feedIds, 0)
        return feedDetails.map { it.toFeedDetail() }
    }

    override fun removes(feedIds: List<String>): List<Media> {
        val details = feedDetailJpaRepository.findAllByFeedIdIn(feedIds.map { it }).map { it.toFeedDetail() }
        feedDetailJpaRepository.deleteAllByFeedIdIn(feedIds.map { it })
        return details.map { it.media }
    }

    override fun append(medias: List<Media>, feedId: String) {
        feedDetailJpaRepository.saveAll(FeedDetailJpaEntity.generate(medias, feedId))
    }
}