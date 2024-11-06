package org.chewing.v1.repository

import org.chewing.v1.jparepository.feed.FeedJpaRepository
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.repository.feed.FeedRepositoryImpl
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

class FeedRepositoryTest2 {
    private val feedJpaRepository: FeedJpaRepository = mock()

    private var feedRepositoryImpl: FeedRepositoryImpl = FeedRepositoryImpl(feedJpaRepository)

    @Test
    fun `피드 업데이트 - 실패(피드가 존재하지 않음)`() {
        val feedId = "feedId"
        whenever(feedJpaRepository.findById(feedId)).thenReturn(Optional.empty())
        val result = feedRepositoryImpl.update(feedId, FeedTarget.UNHIDE)
        assert(result == null)
    }
}
