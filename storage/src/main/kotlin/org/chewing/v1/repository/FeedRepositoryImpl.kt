package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
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
    private val feedCommentsRepository: FeedCommentJpaRepository
) : FeedRepository {
    override fun readFeedWithDetails(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findByIdWithDetails(feedId.value()).map { it.toFeedWithDetails() }.orElse(null)
    }

    override fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean {
        val userFeedId = UserFeedId(userId = userId.value(), feedId = feedId.value())
        return feedLikesRepository.existsById(userFeedId)
    }

    override fun readFeed(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findById(feedId.value()).map { it.toFeed() }.orElse(null)
    }

    override fun readFeedWithWriter(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findByIdWithWriter(feedId.value()).map { it.toFeedWithWriter() }.orElse(null)
    }

    override fun readFeedsWithDetails(userId: User.UserId): List<Feed> {
        return feedJpaRepository.findByWriterIdWithDetails(userId.value()).map { it.toFeedWithDetails() }
    }

    override fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean> {
        val likedFeedIds = feedLikesRepository.findAllByUserIdAndFeedIdIn(userId.value(), feedIds.map { it.value() })
            .map { it.id.feedId }
        return feedIds.associateWith { feedId -> likedFeedIds.contains(feedId.value()) }
    }

    override fun appendFeedComment(feed: Feed, comment: FeedComment) {
        feedCommentsRepository.save(FeedCommentJpaEntity.fromFeedComment(comment, feed))
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun readFeedComment(feedId: Feed.FeedId): List<FeedComment> {
        return feedCommentsRepository.findAllByFeedIdWithWriter(feedId.value()).map { it.toFeedComment() }
    }

    override fun readFeedCommentsWithFeed(commentIds: List<FeedComment.CommentId>): List<Pair<FeedComment, Feed>> {
        return feedCommentsRepository.findAllByIdsWithWriterWithFeed(commentIds.map { it.value() })
            .map { Pair(it.toFeedComment(), it.toFeed()) }
    }

    override fun readUserCommentWithFeed(userId: User.UserId): List<Pair<Feed, FeedComment>> {
        return feedCommentsRepository.findAllByUserIdWithFeed(userId.value()).map {
            Pair(it.toFeedWithDetails(), it.toFeedComment())
        }
    }

    override fun appendFeedLikes(feed: Feed, user: User) {
        val userFeedJpaEntity = UserFeedLikesJpaEntity.fromUserFeed(user, feed)
        feedLikesRepository.saveAndFlush(userFeedJpaEntity)
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun removeFeedLikes(feed: Feed, user: User) {
        feedLikesRepository.deleteById(UserFeedId(user.userId.value(), feed.feedId.value()))
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun updateFeed(feed: Feed) {
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }

    override fun removeFeed(feed: Feed) {
        feedJpaRepository.deleteById(feed.feedId.value())
    }

    override fun removeFeedComments(feed: Feed, commentId: FeedComment.CommentId) {
        feedCommentsRepository.deleteByFeedCommentId(commentId.value())
        feedJpaRepository.saveAndFlush(FeedJpaEntity.fromFeed(feed))
    }
}