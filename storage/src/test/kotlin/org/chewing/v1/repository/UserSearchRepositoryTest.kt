package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jpaentity.friend.UserSearchJpaEntity
import org.chewing.v1.jparepository.user.UserSearchJpaRepository
import org.chewing.v1.repository.user.UserSearchRepositoryImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class UserSearchRepositoryTest : JpaContextTest() {
    @Autowired
    private lateinit var userSearchJpaRepository: UserSearchJpaRepository

    private val userSearchRepositoryImpl: UserSearchRepositoryImpl by lazy {
        UserSearchRepositoryImpl(userSearchJpaRepository)
    }

    @Test
    fun `검색 히스토리 추가`() {
        // given
        val userId = "userId"
        val keyword = "keyword"

        // when
        userSearchRepositoryImpl.appendHistory(userId, keyword)

        // then
        val searchHistory = userSearchJpaRepository.findAllByUserIdOrderByCreatedAt(userId)
        assert(searchHistory.size == 1)
    }

    @Test
    fun `검색 히스토리 조회 A`() {
        // given
        val userId = "userId"
        val keyword = "keyword"
        userSearchJpaRepository.save(UserSearchJpaEntity.fromFriendSearch(userId, keyword))

        // when
        val searchHistory = userSearchRepositoryImpl.readSearchHistory(userId)

        // then
        assert(searchHistory.size == 1)
    }
}