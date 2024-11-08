package org.chewing.v1.service

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.feed.feed.FeedUpdater
import org.chewing.v1.implementation.feed.like.*
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.repository.feed.FeedLikesRepository
import org.chewing.v1.service.feed.FeedLikesService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.dao.OptimisticLockingFailureException

class FeedLikesServiceTest {
    private val feedLikesRepository: FeedLikesRepository = mock()
    private val feedUpdater: FeedUpdater = mock()

    private val feedLikeAppender = FeedLikeAppender(feedLikesRepository)
    private val feedLikeChecker = FeedLikeChecker(feedLikesRepository)
    private val feedLikeRemover = FeedLikeRemover(feedLikesRepository)
    private val feedLikeProcessor = FeedLikeProcessor(feedLikeAppender, feedLikeRemover, feedUpdater)
    private val feedLikeHandler = FeedLikeHandler(feedLikeProcessor)
    private val feedLikeValidator = FeedLikeValidator(feedLikesRepository)
    private val feedLikeService = FeedLikesService(feedLikeValidator, feedLikeHandler, feedLikeRemover, feedLikeChecker)

    @Test
    fun `피드 좋아요 성공`() {
        val userId = "userId"
        val feedId = "feedId"
        val feedTarget = FeedTarget.LIKES

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(false)

        feedLikeService.like(feedId, userId, feedTarget)
    }

    @Test
    fun `피드 좋아요 실패 - 이미 좋아요 함`() {
        val userId = "userId"
        val feedId = "feedId"
        val feedTarget = FeedTarget.LIKES

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(true)

        val exception = assertThrows<ConflictException> {
            feedLikeService.like(feedId, userId, feedTarget)
        }

        assert(exception.errorCode == ErrorCode.FEED_ALREADY_LIKED)
    }

    @Test
    fun `피드 좋아요 실패 - 낙관적 락`() {
        val userId = "userId"
        val feedId = "feedId"
        val feedTarget = FeedTarget.LIKES

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(false)
        whenever(feedUpdater.update(feedId, feedTarget)).thenThrow(OptimisticLockingFailureException(""))

        val result = assertThrows<ConflictException> {
            feedLikeService.like(feedId, userId, feedTarget)
        }

        verify(feedUpdater, times(5)).update(feedId, feedTarget)

        assert(result.errorCode == ErrorCode.FEED_LIKED_FAILED)
    }

    @Test
    fun `피드 좋아요 취소 성공`() {
        val userId = "userId"
        val feedId = "feedId"
        val feedTarget = FeedTarget.UNLIKES

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(true)

        feedLikeService.unlike(feedId, userId, feedTarget)
    }

    @Test
    fun `피드 좋아요 취소 실패 - 이미 좋아요 안함`() {
        val userId = "userId"
        val feedId = "feedId"
        val feedTarget = FeedTarget.UNLIKES

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(false)

        val exception = assertThrows<ConflictException> {
            feedLikeService.unlike(feedId, userId, feedTarget)
        }

        assert(exception.errorCode == ErrorCode.FEED_ALREADY_UNLIKED)
    }

    @Test
    fun `피드 좋아요 취소 실패 - 낙관적 락`() {
        val userId = "userId"
        val feedId = "feedId"
        val feedTarget = FeedTarget.UNLIKES

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(true)
        whenever(feedUpdater.update(feedId, feedTarget)).thenThrow(OptimisticLockingFailureException(""))

        val result = assertThrows<ConflictException> {
            feedLikeService.unlike(feedId, userId, feedTarget)
        }

        verify(feedUpdater, times(5)).update(feedId, feedTarget)

        assert(result.errorCode == ErrorCode.FEED_UNLIKED_FAILED)
    }

    @Test
    fun `피드 좋아요 여부 - 좋아요 함`() {
        val userId = "userId"
        val feedId = "feedId"

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(true)

        val result = feedLikeService.checkLike(feedId, userId)

        assert(result)
    }

    @Test
    fun `피드 좋아요 여부 - 좋아요 안함`() {
        val userId = "userId"
        val feedId = "feedId"

        whenever(feedLikesRepository.checkLike(feedId, userId)).thenReturn(false)

        val result = feedLikeService.checkLike(feedId, userId)

        assert(!result)
    }

    @Test
    fun `모든 좋아요 삭제`() {
        val feedId = "feedId"

        assertDoesNotThrow {
            feedLikeService.unlikes(listOf(feedId))
        }
    }
}
