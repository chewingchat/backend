package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedDetailJpaEntity
import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.jpaentity.user.UserFeedId
import org.chewing.v1.jpaentity.user.UserFeedLikesJpaEntity
import org.chewing.v1.jparepository.FeedCommentJpaRepository
import org.chewing.v1.jparepository.FeedDetailJpaRepository
import org.chewing.v1.jparepository.FeedJpaRepository
import org.chewing.v1.jparepository.UserFeedLikesJpaRepository
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
class FeedRepositoryImpl(
    private val feedJpaRepository: FeedJpaRepository,
    private val feedLikesRepository: UserFeedLikesJpaRepository,
    private val feedDetailJpaRepository: FeedDetailJpaRepository,
    private val commentJpaRepository: FeedCommentJpaRepository
) : FeedRepository {
    override fun isAlreadyLiked(feedId: Feed.FeedId, userId: User.UserId): Boolean {
        val userFeedId = UserFeedId(userId = userId.value(), feedId = feedId.value())
        return feedLikesRepository.existsById(userFeedId)
    }

    override fun readFeed(feedId: Feed.FeedId): Feed? {
        return feedJpaRepository.findById(feedId.value()).map { it.toFeed() }.orElse(null)
    }

    override fun readFeeds(feedIds: List<Feed.FeedId>): List<Feed> {
        return feedJpaRepository.findAllById(feedIds.map { it.value() }).map { it.toFeed() }
    }

    override fun readFeedByUserId(userId: User.UserId): List<Feed> {
        return feedJpaRepository.findByUserId(userId.value()).map { it.toFeed() }
    }

    override fun readFeedByCommentId(commentId: Comment.CommentId): Feed? {
        commentJpaRepository.findById(commentId.value()).orElseThrow().let {
            return feedJpaRepository.findById(it.feedId).orElse(null).toFeed()
        }
    }

    override fun readFeedDetails(feedId: Feed.FeedId): List<FeedDetail> {
        return feedDetailJpaRepository.findAllByFeedId(feedId.value()).map { it.toFeedDetail() }
    }

    override fun readFeedsDetails(feedIds: List<Feed.FeedId>): Map<Feed.FeedId, List<FeedDetail>> {
        val feedIdStrings = feedIds.map { it.value() }

        val feedDetails = feedDetailJpaRepository.findAllByFeedIdIn(feedIdStrings)

        return feedDetails.groupBy { Feed.FeedId.of(it.feedId) }
            .mapValues { entry -> entry.value.map { it.toFeedDetail() } }
    }

    override fun readFeedsByOwner(feedIds: List<Feed.FeedId>,userIds:List<User.UserId>): List<Pair<Feed, User.UserId>> {
        return feedJpaRepository.findAllByFeedIdInAndUserIdIn(feedIds.map { it.value() }, userIds.map { it.value() }).map { Pair(it.toFeed(),it.toUserId())  }
    }


    override fun readFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean> {
        val likedFeedIds = feedLikesRepository.findAllByUserIdAndFeedIdIn(userId.value(), feedIds.map { it.value() })
            .map { it.id.feedId }
        return feedIds.associateWith { feedId -> likedFeedIds.contains(feedId.value()) }
    }

    override fun isFeedOwner(feedId: Feed.FeedId, userId: User.UserId): Boolean {
        return feedJpaRepository.existsByFeedIdAndUserId(feedId.value(), userId.value())
    }

    override fun isFeedsOwner(feedIds: List<Feed.FeedId>, userId: User.UserId): Boolean {
        return feedJpaRepository.existsAllByFeedIdInAndUserId(feedIds.map { it.value() }, userId.value())
    }

    override fun appendFeedLikes(feed: Feed, user: User) {
        val userFeedJpaEntity = UserFeedLikesJpaEntity.fromUserFeed(user, feed)
        feedLikesRepository.saveAndFlush(userFeedJpaEntity)
    }

    override fun removeFeedLikes(feed: Feed, user: User) {
        feedLikesRepository.deleteById(UserFeedId(user.userId.value(), feed.id.value()))
    }

    override fun updateFeed(feed: Feed, target: FeedTarget) {
        feedJpaRepository.findById(feed.id.value()).orElseThrow().let {
            when (target) {
                FeedTarget.LIKES -> it.updateLikes()
                FeedTarget.UNLIKES -> it.updateUnLikes()
                FeedTarget.COMMENTS -> it.updateComments()
                FeedTarget.UNCOMMENTS -> it.updateUnComments()
            }
            feedJpaRepository.saveAndFlush(it)
        }
    }

    override fun removeFeeds(feedIds: List<Feed.FeedId>) {
        feedJpaRepository.deleteAllById(feedIds.map { it.value() })
    }

    override fun removeFeedsDetail(feedIds: List<Feed.FeedId>): List<Media> {
        val details = feedDetailJpaRepository.findAllByFeedIdIn(feedIds.map { it.value() }).map { it.toFeedDetail() }
        feedDetailJpaRepository.deleteAllById(feedIds.map { it.value() })
        return details.map { it.media }
    }

    override fun appendFeed(medias: List<Media>, user: User, topic: String): Feed.FeedId {
        val feedId = feedJpaRepository.save(FeedJpaEntity.generate(topic, user)).toFeedId()
        feedDetailJpaRepository.saveAll(FeedDetailJpaEntity.generate(medias, feedId)).map { it.toDetailId() }
        return feedId
    }
}