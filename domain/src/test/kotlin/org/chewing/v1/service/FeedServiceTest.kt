package org.chewing.v1.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.chewing.v1.TestDataFactory
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.feed.feed.*
import org.chewing.v1.implementation.media.FileHandler
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.repository.feed.FeedDetailRepository
import org.chewing.v1.repository.feed.FeedRepository
import org.chewing.v1.service.feed.FeedService
import org.chewing.v1.util.AsyncJobExecutor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.dao.OptimisticLockingFailureException

class FeedServiceTest {
    private val feedRepository: FeedRepository = mock()
    private val feedDetailRepository: FeedDetailRepository = mock()
    private val fileHandler: FileHandler = mock()

    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val asyncJobExecutor = AsyncJobExecutor(ioScope)
    private val feedReader: FeedReader = FeedReader(feedRepository, feedDetailRepository)
    private val feedAppender: FeedAppender = FeedAppender(feedRepository, feedDetailRepository)
    private val feedValidator: FeedValidator = FeedValidator(feedRepository)
    private val feedUpdater: FeedUpdater = FeedUpdater(feedRepository)
    private val feedEnricher: FeedEnricher = FeedEnricher()
    private val feedRemover: FeedRemover = FeedRemover(feedRepository, feedDetailRepository)
    private val feedHandler: FeedHandler = FeedHandler(feedUpdater, asyncJobExecutor)
    private val feedService: FeedService =
        FeedService(feedReader, feedHandler, feedAppender, feedValidator, fileHandler, feedEnricher, feedRemover)

    @Test
    fun `피드를 가져온다`() {
        val feedId = "feedId"
        val userId = "userId"
        val feedDetailId = "feedDetailId"
        val feed = TestDataFactory.createFeedInfo(feedId, userId)
        val feedDetail = TestDataFactory.createFeedDetail(feedId, feedDetailId, 0)

        whenever(feedRepository.read(feedId)).thenReturn(feed)
        whenever(feedDetailRepository.read(feedId)).thenReturn(listOf(feedDetail))

        val result = feedService.getFeed(feedId)

        assert(result.feed.feedId == feedId)
        assert(result.feedDetails.size == 1)
        assert(result.feedDetails[0].feedDetailId == feedDetailId)
        assert(result.feedDetails[0].feedId == feedId)
        assert(result.feed.userId == userId)
    }

    @Test
    fun `피드들을 분류해서 가져온다`() {
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val userId = "userId"
        val feedDetailId1 = "feedDetailId1"
        val feedDetailId2 = "feedDetailId2"
        val feedDetailId3 = "feedDetailId3"
        val feedDetailId4 = "feedDetailId4"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, userId)
        val feedDetail1 = TestDataFactory.createFeedDetail(feedId1, feedDetailId1, 0)
        val feedDetail2 = TestDataFactory.createFeedDetail(feedId1, feedDetailId2, 1)
        val feedDetail3 = TestDataFactory.createFeedDetail(feedId2, feedDetailId3, 0)
        val feedDetail4 = TestDataFactory.createFeedDetail(feedId2, feedDetailId4, 1)

        whenever(feedRepository.reads(listOf(feedId1, feedId2))).thenReturn(listOf(feed1, feed2))
        whenever(feedDetailRepository.readsFirstIndex(listOf(feedId1, feedId2))).thenReturn(
            listOf(
                feedDetail1,
                feedDetail2,
                feedDetail3,
                feedDetail4
            )
        )

        val result = feedService.getFeeds(listOf(feedId1, feedId2))

        assert(result.size == 2)
        assert(result[0].feed.feedId == feedId1)
        assert(result[0].feedDetails.size == 2)
        assert(result[0].feedDetails[0].feedDetailId == feedDetailId1)
        assert(result[0].feedDetails[1].feedDetailId == feedDetailId2)
        assert(result[1].feed.feedId == feedId2)
        assert(result[1].feedDetails.size == 2)
        assert(result[1].feedDetails[0].feedDetailId == feedDetailId3)
        assert(result[1].feedDetails[1].feedDetailId == feedDetailId4)
    }

    @Test
    fun `본인 피드들을 가져온다`() {
        val userId = "userId"
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val feedDetailId1 = "feedDetailId1"
        val feedDetailId2 = "feedDetailId2"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, userId)
        val feedDetail1 = TestDataFactory.createFeedDetail(feedId1, feedDetailId1, 0)
        val feedDetail2 = TestDataFactory.createFeedDetail(feedId2, feedDetailId2, 1)

        whenever(feedRepository.readsOwned(userId, FeedStatus.NOT_HIDDEN)).thenReturn(listOf(feed1, feed2))
        whenever(feedDetailRepository.readsFirstIndex(listOf(feedId1, feedId2))).thenReturn(
            listOf(
                feedDetail1,
                feedDetail2,
            )
        )

        val result = feedService.getOwnedFeeds(userId, FeedStatus.NOT_HIDDEN)

        assert(result.size == 2)
        assert(result[0].feed.feedId == feedId1)
        assert(result[0].feedDetails.size == 1)
        assert(result[0].feedDetails[0].feedDetailId == feedDetailId1)
        assert(result[1].feed.feedId == feedId2)
        assert(result[1].feedDetails.size == 1)
        assert(result[1].feedDetails[0].feedDetailId == feedDetailId2)
    }

    @Test
    fun `피드들을 삭제에 성공한다`() {
        val userId = "userId"
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, userId)

        whenever(feedRepository.reads(listOf(feedId1, feedId2))).thenReturn(listOf(feed1, feed2))

        assertDoesNotThrow { feedService.removes(userId, listOf(feedId1, feedId2)) }
    }

    @Test
    fun `피드들 삭제 에 실패 - 잘봇된 접근 본인 소유의 피드가 아님`() {
        val userId = "userId"
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, "anotherUserId")

        whenever(feedRepository.reads(listOf(feedId1, feedId2))).thenReturn(listOf(feed1, feed2))

        assertThrows<ConflictException> {
            feedService.removes(userId, listOf(feedId1, feedId2))
        }
    }

    @Test
    fun `피드 숨기기 기능 성공`() {
        val userId = "userId"
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, userId)

        whenever(feedRepository.reads(listOf(feedId1, feedId2))).thenReturn(listOf(feed1, feed2))

        assertDoesNotThrow { feedService.changeHide(userId, listOf(feedId1, feedId2), FeedTarget.UNHIDE) }
        assertDoesNotThrow { feedService.changeHide(userId, listOf(feedId1, feedId2), FeedTarget.HIDE) }
    }

    @Test
    fun `피드를 숨기기 기능 실패 - 잘못된 접근 본인 소유의 피드가 아님`() {
        val userId = "userId"
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, userId)

        whenever(feedRepository.reads(listOf(feedId1, feedId2))).thenReturn(listOf(feed1, feed2))

        val exception = assertThrows<ConflictException>() {
            feedService.changeHide("anotherUserId", listOf(feedId1, feedId2), FeedTarget.HIDE)
        }

        assert(exception.errorCode == ErrorCode.FEED_IS_NOT_OWNED)
    }

    @Test
    fun `피드 숨기기 기능 실패 - 낙관적락 실패`() {
        val userId = "userId"
        val feedId1 = "feedId1"
        val feedId2 = "feedId2"
        val feed1 = TestDataFactory.createFeedInfo(feedId1, userId)
        val feed2 = TestDataFactory.createFeedInfo(feedId2, userId)

        whenever(feedRepository.reads(listOf(feedId1, feedId2))).thenReturn(listOf(feed1, feed2))
        whenever(
            feedRepository.update(
                feedId1,
                FeedTarget.HIDE
            )
        ).thenThrow(OptimisticLockingFailureException::class.java)
        whenever(
            feedRepository.update(
                feedId2,
                FeedTarget.HIDE
            )
        ).thenThrow(OptimisticLockingFailureException::class.java)

        feedService.changeHide(userId, listOf(feedId1, feedId2), FeedTarget.HIDE)

        verify(feedRepository, times(5)).update(feedId1, FeedTarget.HIDE)
        verify(feedRepository, times(5)).update(feedId2, FeedTarget.HIDE)
    }

    @Test
    fun `피드를 추가한다`() {
        val userId = "userId"
        val topic = "topic"
        val fileData = TestDataFactory.createFileData()
        val media = TestDataFactory.createProfileMedia()

        whenever(fileHandler.handleNewFiles(userId, listOf(fileData), FileCategory.FEED)).thenReturn(listOf(media))

        assertDoesNotThrow {
            feedService.make(userId, listOf(fileData), topic, FileCategory.FEED)
        }
    }
}
