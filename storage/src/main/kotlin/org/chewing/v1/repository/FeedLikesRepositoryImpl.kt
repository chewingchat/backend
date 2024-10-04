package org.chewing.v1.repository

import org.chewing.v1.jpaentity.user.UserFeedId
import org.chewing.v1.jpaentity.user.UserFeedLikesJpaEntity
import org.chewing.v1.jparepository.FeedLikesJpaRepository
import org.chewing.v1.model.feed.FeedInfo
import org.springframework.stereotype.Repository

@Repository
internal class FeedLikesRepositoryImpl(
    private val feedLikesRepository: FeedLikesJpaRepository,
) : FeedLikesRepository {
    override fun isAlreadyLiked(feedId: String, userId: String): Boolean {
        val userFeedId = UserFeedId(userId = userId, feedId = feedId)
        return feedLikesRepository.existsById(userFeedId)
    }

    override fun likes(feedId: String, userId: String) {
        val userFeedJpaEntity = UserFeedLikesJpaEntity.fromUserFeed(userId, feedId)
        feedLikesRepository.saveAndFlush(userFeedJpaEntity)
    }

    override fun unlikes(feedId: String, userId: String) {
        feedLikesRepository.deleteById(UserFeedId(userId, feedId))
    }

    override fun unlikeAll(feedIds: List<String>) {
        feedLikesRepository.deleteAllByUserFeedIdFeedIdIn(feedIds)
    }

    override fun checkLike(feedId: String, userId: String): Boolean {
        return feedLikesRepository.existsById(UserFeedId(userId, feedId))
    }
}