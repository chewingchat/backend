package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jpaentity.user.FeedLikeId
import org.chewing.v1.jparepository.feed.FeedLikesJpaRepository
import org.chewing.v1.repository.feed.FeedLikesRepositoryImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class FeedLikesRepositoryTest : JpaContextTest() {
    @Autowired
    private lateinit var feedLikesJpaRepository: FeedLikesJpaRepository

    private val feedLikesRepositoryImpl: FeedLikesRepositoryImpl by lazy {
        FeedLikesRepositoryImpl(feedLikesJpaRepository)
    }

    @Test
    fun `좋아요를 추가해야 한다`() {
        val feedId = "feedId"
        val userId = "userId"
        feedLikesRepositoryImpl.likes(feedId, userId)
        val result = feedLikesJpaRepository.findById(FeedLikeId(userId, feedId))
        assert(result.isPresent)
    }
    @Test
    fun `좋아요를 취소해야 한다`() {
        val feedId = "feedId"
        val userId = "userId"
        feedLikesRepositoryImpl.likes(feedId, userId)
        feedLikesRepositoryImpl.unlikes(feedId, userId)
        val result = feedLikesJpaRepository.findById(FeedLikeId(userId, feedId))
        assert(!result.isPresent)
    }

    @Test
    fun `좋아요를 확인해야 한다`() {
        val feedId = "feedId"
        val userId = "userId"
        feedLikesRepositoryImpl.likes(feedId, userId)
        assert(feedLikesRepositoryImpl.checkLike(feedId, userId))
    }

    @Test
    fun `좋아요를 확인하지 않아야 한다`() {
        val feedId = "feedId"
        val userId = "userId"
        assert(!feedLikesRepositoryImpl.checkLike(feedId, userId))
    }

    @Test
    fun `좋아요를 모두 취소해야 한다`() {
        val feedId = "feedId"
        val userId = "userId"
        feedLikesRepositoryImpl.likes(feedId, userId)
        feedLikesRepositoryImpl.unlikeAll(listOf(feedId))
        val result = feedLikesJpaRepository.findById(FeedLikeId(userId, feedId))
        assert(!result.isPresent)
    }
}
