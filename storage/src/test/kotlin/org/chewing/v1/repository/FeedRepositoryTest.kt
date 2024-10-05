package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.FeedJpaRepository
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.repository.support.TestDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class FeedRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var feedJpaRepository: FeedJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val feedRepositoryImpl: FeedRepositoryImpl by lazy {
        FeedRepositoryImpl(feedJpaRepository)
    }

    @Test
    fun `피드를 추가해야 한다`() {
        val userId = "userId"
        val topic = "topic"
        val result = feedRepositoryImpl.append(userId, topic)
        assert(result.isNotEmpty())
    }

    @Test
    fun `피드를 조회해야 한다`() {
        val userId = "userId2"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        val result = feedRepositoryImpl.read(feedInfo.feedId)
        assert(result != null)
        assert(result!!.topic == feedInfo.topic)
    }

    @Test
    fun `피드목록을 조회해야 한다`() {
        val userId = "userId3"
        val feedInfoList = testDataGenerator.feedEntityDataList(userId)
        val result = feedRepositoryImpl.reads(feedInfoList.map { it.feedId })
        assert(result.isNotEmpty())
        assert(result.size == feedInfoList.size)
        assert(result.map { it.uploadAt } == result.map { it.uploadAt }.sorted())
    }

    @Test
    fun `피드를 삭제해야 한다`() {
        val userId = "userId5"
        val feedInfoList = testDataGenerator.feedEntityDataList(userId)
        feedRepositoryImpl.removes(feedInfoList.map { it.feedId })
        val result = feedJpaRepository.findAllById(feedInfoList.map { it.feedId })
        assert(result.isEmpty())
    }

    @Test
    fun `소유자의 피드를 삭제해야 한다`() {
        val userId = "userId6"
        val feedInfoList = testDataGenerator.feedEntityDataList(userId)
        feedRepositoryImpl.removesOwned(userId)
        val result = feedJpaRepository.findAllById(feedInfoList.map { it.feedId })
        assert(result.isEmpty())
    }

    @Test
    fun `피드 좋아요를 추가해야 한다`() {
        val userId = "userId7"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.LIKES)
        val result = feedRepositoryImpl.read(feedInfo.feedId)
        assert(result!!.likes == feedInfo.likes + 1)
    }

    @Test
    fun `피드 좋아요를 취소해야 한다`() {
        val userId = "userId8"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.LIKES)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.UNLIKES)
        val result = feedJpaRepository.findById(feedInfo.feedId).orElse(null).toFeedInfo()
        assert(result.likes == feedInfo.likes)
    }

    @Test
    fun `피드 댓글을 추가해야 한다`() {
        val userId = "userId9"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.COMMENTS)
        val result = feedJpaRepository.findById(feedInfo.feedId).orElse(null).toFeedInfo()
        assert(result.comments == feedInfo.comments + 1)
    }

    @Test
    fun `피드 댓글을 취소해야 한다`() {
        val userId = "userId10"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.COMMENTS)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.UNCOMMENTS)
        val result = feedJpaRepository.findById(feedInfo.feedId).orElse(null).toFeedInfo()
        assert(result.comments == feedInfo.comments)
    }

    @Test
    fun `피드 숨김을 해제해야 한다`() {
        val userId = "userId11"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.HIDE)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.UNHIDE)
        val result = feedJpaRepository.findAllByUserIdAndHideFalseOrderByCreatedAtAsc(userId)
        assert(result.isNotEmpty())
        assert(result.size == 1)
    }

    @Test
    fun `피드 숨김을 해야 한다`() {
        val userId = "userId12"
        val feedInfo = testDataGenerator.feedEntityData(userId)
        feedRepositoryImpl.update(feedInfo.feedId, FeedTarget.HIDE)
        val result = feedJpaRepository.findAllByUserIdAndHideTrueOrderByCreatedAtAsc(userId)
        assert(result.isNotEmpty())
        assert(result.size == 1)
    }

    @Test
    fun `소유자의 모든 피드를 조회해야 한다`() {
        val userId = "userId13"
        val feedInfoList = testDataGenerator.feedEntityDataList(userId)
        val result = feedRepositoryImpl.readsOwned(userId, FeedStatus.ALL)
        assert(result.isNotEmpty())
        assert(result.size == feedInfoList.size)
    }

    @Test
    fun `소유자의 숨김 피드를 조회해야 한다`() {
        val userId = "userId14"
        val feedInfoList = testDataGenerator.feedEntityDataList(userId)
        feedRepositoryImpl.update(feedInfoList[0].feedId, FeedTarget.HIDE)
        val result = feedRepositoryImpl.readsOwned(userId, FeedStatus.HIDDEN)
        assert(result.isNotEmpty())
        assert(result.size == 1)
    }

    @Test
    fun `소유자의 숨김하지 않은 피드를 조회해야 한다`() {
        val userId = "userId15"
        val feedInfoList = testDataGenerator.feedEntityDataList(userId)
        feedRepositoryImpl.update(feedInfoList[0].feedId, FeedTarget.HIDE)
        val result = feedRepositoryImpl.readsOwned(userId, FeedStatus.NOT_HIDDEN)
        assert(result.isNotEmpty())
        assert(result.size == feedInfoList.size - 1)
    }
}