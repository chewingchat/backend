package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedDetailJpaEntity
import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.jpaentity.user.UserFeedId
import org.chewing.v1.jpaentity.user.UserFeedLikesJpaEntity
import org.chewing.v1.jparepository.FeedDetailJpaRepository
import org.chewing.v1.jparepository.FeedJpaRepository
import org.chewing.v1.jparepository.UserFeedLikesJpaRepository
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
internal class FeedRepositoryImpl(
    private val feedJpaRepository: FeedJpaRepository,
    private val feedLikesRepository: UserFeedLikesJpaRepository,
    private val feedDetailJpaRepository: FeedDetailJpaRepository,
) : FeedRepository {
    override fun isAlreadyLiked(feedId: String, userId: String): Boolean {
        val userFeedId = UserFeedId(userId = userId, feedId = feedId)
        return feedLikesRepository.existsById(userFeedId)
    }

    override fun read(feedId: String): FeedInfo? {
        return feedJpaRepository.findById(feedId).map { it.toFeedInfo() }.orElse(null)
    }

    override fun reads(feedIds: List<String>): List<FeedInfo> {
        return feedJpaRepository.findAllById(feedIds.map { it }).map { it.toFeedInfo() }
    }

    override fun readsByUserId(userId: String): List<FeedInfo> {
        return feedJpaRepository.findByUserId(userId).map { it.toFeedInfo() }
    }

    override fun readDetails(feedId: String): List<FeedDetail> {
        return feedDetailJpaRepository.findAllByFeedId(feedId).map { it.toFeedDetail() }
    }

    override fun readsDetails(feedIds: List<String>):  List<FeedDetail> {
        val feedDetails = feedDetailJpaRepository.findAllByFeedIdIn(feedIds)
        return feedDetails.map { it.toFeedDetail() }
    }

    override fun readsByOwner(feedIds: List<String>, userIds: List<String>): List<FeedInfo> {
        return feedJpaRepository.findAllByFeedIdInAndUserIdIn(feedIds.map { it }, userIds.map { it })
            .map { it.toFeedInfo() }
    }

    override fun readsLike(feedIds: List<String>, userId: String): List<String> {
        return feedLikesRepository.findAllByUserIdAndFeedIdIn(userId, feedIds.map { it }).map { it.userFeedId.feedId }
    }

    override fun isOwner(feedId: String, userId: String): Boolean {
        return feedJpaRepository.existsByFeedIdAndUserId(feedId, userId)
    }

    override fun isAllOwner(feedIds: List<String>, userId: String): Boolean {
        return feedJpaRepository.existsAllByFeedIdInAndUserId(feedIds.map { it }, userId)
    }

    override fun likes(feedInfo: FeedInfo, user: User) {
        val userFeedJpaEntity = UserFeedLikesJpaEntity.fromUserFeed(user, feedInfo)
        feedLikesRepository.saveAndFlush(userFeedJpaEntity)
    }

    override fun unlikes(feedInfo: FeedInfo, user: User) {
        feedLikesRepository.deleteById(UserFeedId(user.userId, feedInfo.feedId))
    }

    override fun update(feedId: String, target: FeedTarget) {
        feedJpaRepository.findById(feedId).orElseThrow().let {
            when (target) {
                FeedTarget.LIKES -> it.updateLikes()
                FeedTarget.UNLIKES -> it.updateUnLikes()
                FeedTarget.COMMENTS -> it.updateComments()
                FeedTarget.UNCOMMENTS -> it.updateUnComments()
            }
            feedJpaRepository.saveAndFlush(it)
        }
    }

    override fun removes(feedIds: List<String>) {
        feedJpaRepository.deleteAllById(feedIds.map { it })
    }

    override fun removesDetails(feedIds: List<String>): List<Media> {
        val details = feedDetailJpaRepository.findAllByFeedIdIn(feedIds.map { it }).map { it.toFeedDetail() }
        feedDetailJpaRepository.deleteAllById(feedIds.map { it })
        return details.map { it.media }
    }

    override fun append(medias: List<Media>, user: User, topic: String): String {
        val feedId = feedJpaRepository.save(FeedJpaEntity.generate(topic, user)).toFeedId()
        feedDetailJpaRepository.saveAll(FeedDetailJpaEntity.generate(medias, feedId)).map { it.toDetailId() }
        return feedId
    }
}