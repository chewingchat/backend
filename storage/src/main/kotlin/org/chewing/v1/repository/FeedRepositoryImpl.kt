package org.chewing.v1.repository

import org.chewing.v1.jpaentity.user.UserFeedId
import org.chewing.v1.jparepository.FeedJpaRepository
import org.chewing.v1.jparepository.UserFeedLikesJpaRepository
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
class FeedRepositoryImpl(
    private val feedJpaRepository: FeedJpaRepository,
    private val feedLikesRepository: UserFeedLikesJpaRepository
) : FeedRepository {
    override fun readFeedWithDetails(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findByIdWithDetails(feedId.value()).map { it.toFeedWithDetails() }.orElse(null)
    }

    override fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean {
        val userFeedId = UserFeedId(userId = userId.value(), feedId = feedId.value())
        return feedLikesRepository.existsById(userFeedId)
    }

    override fun readFeedsWithDetails(userId: User.UserId): List<Feed> {
        return feedJpaRepository.findByWriterIdWithDetails(userId.value()).map { it.toFeedWithDetails() }
    }

    override fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean> {
        val likedFeedIds = feedLikesRepository.findAllByUserIdAndFeedIdIn(userId.value(), feedIds.map { it.value() })
            .map { it.id.feedId }
        return feedIds.associateWith { feedId -> likedFeedIds.contains(feedId.value()) }
    }

}