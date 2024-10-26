package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.feed.FeedDetailJpaRepository
import org.chewing.v1.repository.feed.FeedDetailRepositoryImpl
import org.chewing.v1.repository.support.MediaProvider
import org.chewing.v1.repository.support.JpaDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class FeedDetailRepositoryTest : JpaContextTest() {
    @Autowired
    private lateinit var feedDetailJpaRepository: FeedDetailJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val feedDetailRepositoryImpl: FeedDetailRepositoryImpl by lazy {
        FeedDetailRepositoryImpl(feedDetailJpaRepository)
    }

    @Test
    fun `피드 상세를 추가해야 한다`() {
        val feedId = "feedId"
        val medias = MediaProvider.buildFeedContents()
        feedDetailRepositoryImpl.append(medias, feedId)
        val result = feedDetailJpaRepository.findAllByFeedIdOrderByFeedIndex(feedId)
        assert(result.isNotEmpty())
        assert(result.size == medias.size)
    }

    @Test
    fun `피드 상세를 Index 기준으로 순서대로 조회해야 한다`() {
        val feedId = "feedId2"
        val feedDetails = jpaDataGenerator.feedDetailEntityDataAsc(feedId)
        val result = feedDetailRepositoryImpl.read(feedId)
        assert(result.isNotEmpty())
        assert(result.size == feedDetails.size)
        result.forEachIndexed { index, feedDetail ->
            assert(feedDetail.media.index == index)
        }
    }

    @Test
    fun `피드 상세를 Index 기준으로 첫번째만 조회해야 한다`() {
        val feedIds = listOf("feedId3", "feedId4")
        jpaDataGenerator.feedDetailEntityDataAsc("feedId3")
        jpaDataGenerator.feedDetailEntityDataAsc("feedId4")
        val result = feedDetailRepositoryImpl.readsFirstIndex(feedIds)
        assert(result.isNotEmpty())
        assert(result.size == feedIds.size)
        result.forEach { feedDetail ->
            assert(feedDetail.media.index == 0)
        }
    }

    @Test
    fun `피드 상세를 삭제해야 한다`() {
        val feedIds = listOf("feedId5", "feedId6")
        val feedDetails = jpaDataGenerator.feedDetailEntityDataAsc("feedId5")
        val feedDetails2 = jpaDataGenerator.feedDetailEntityDataAsc("feedId6")
        val result = feedDetailRepositoryImpl.removes(feedIds)
        assert(result.isNotEmpty())
        assert(result.size == feedDetails.size + feedDetails2.size)
        val result2 = feedDetailJpaRepository.findAllByFeedIdIn(feedIds)
        assert(result2.isEmpty())
    }
}