package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.implementation.search.SearchAppender
import org.chewing.v1.implementation.search.SearchReader
import org.chewing.v1.repository.user.UserSearchRepository
import org.chewing.v1.service.search.SearchService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchServiceTest {
    private val userSearchRepository: UserSearchRepository = mock()
    private val searchAppender: SearchAppender = SearchAppender(userSearchRepository)
    private val searchReader = SearchReader(userSearchRepository)

    private val searchService = SearchService(searchReader, searchAppender)

    @Test
    fun `유저의 검색 키워드를 추가한다`() {
        val userId = "userId"
        val keyword = "keyword"
        assertDoesNotThrow {
            searchService.createSearchKeyword(userId, keyword)
        }
    }

    @Test
    fun `유저의 검색 키워드를 가져온다`() {
        val userId = "userId"
        val userSearch = TestDataFactory.createUserSearch(userId)
        whenever(userSearchRepository.readSearchHistory(userId)).thenReturn(listOf(userSearch))

        val result = assertDoesNotThrow {
            searchService.getSearchKeywords(userId)
        }

        assert(result.size == 1)
    }
}