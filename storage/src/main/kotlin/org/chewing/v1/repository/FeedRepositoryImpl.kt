package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.jpaentity.user.UserFeedId
import org.chewing.v1.jpaentity.user.UserFeedLikesJpaEntity
import org.chewing.v1.jparepository.FeedCommentJpaRepository
import org.chewing.v1.jparepository.FeedJpaRepository
import org.chewing.v1.jparepository.UserFeedLikesJpaRepository
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Repository

@Repository
class FeedRepositoryImpl(
    private val feedJpaRepository: FeedJpaRepository,
    private val feedLikesRepository: UserFeedLikesJpaRepository,
    private val feedCommentJpaRepository: FeedCommentJpaRepository
) : FeedRepository {
    override fun readFulledFeed(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findByIdWithDetails(feedId.value()).map { it.toFeedWithDetails() }.orElse(null)
    }

    override fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean {
        val userFeedId = UserFeedId(userId = userId.value(), feedId = feedId.value())
        return feedLikesRepository.existsById(userFeedId)
    }

    override fun readFeed(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findById(feedId.value()).map { it.toFeed() }.orElse(null)
    }

    override fun readFulledFeedsByUserId(userId: User.UserId): List<Feed> {
        return feedJpaRepository.findByWriterIdWithDetails(userId.value()).map { it.toFeedWithDetails() }
    }

    override fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean> {
        val likedFeedIds = feedLikesRepository.findAllByUserIdAndFeedIdIn(userId.value(), feedIds.map { it.value() })
            .map { it.id.feedId }
        return feedIds.associateWith { feedId -> likedFeedIds.contains(feedId.value()) }
    }

    override fun appendFeedLikes(feed: Feed, user: User) {
        val userFeedJpaEntity = UserFeedLikesJpaEntity.fromUserFeed(user, feed)
        feedLikesRepository.saveAndFlush(userFeedJpaEntity)
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun removeFeedLikes(feed: Feed, user: User) {
        feedLikesRepository.deleteById(UserFeedId(user.userId.value(), feed.id.value()))
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun updateFeed(feed: Feed) {
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun removeFeeds(feedIds: List<Feed.FeedId>) {
        feedJpaRepository.deleteAllById(feedIds.map { it.value() })
    }

    override fun readFulledFeedByCommentId(commentId: FeedComment.CommentId): Feed {
        return feedCommentJpaRepository.findByIdWithFeedAndWriter(commentId.value()).toFeed()
    }

    override fun appendFeed(feed: Feed): Feed.FeedId {
        val feedId = feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed)).feedId
        return Feed.FeedId.of(feedId)
    }

    override fun readFulledFeeds(feedIds: List<Feed.FeedId>): List<Feed> {
        return feedJpaRepository.findByIdsWithDetails(feedIds.map { it.value() }).map { it.toFeedWithDetails() }
    }
}