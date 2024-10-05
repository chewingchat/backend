package org.chewing.v1.repository

import org.chewing.v1.jpaentity.friend.UserSearchJpaEntity
import org.chewing.v1.jparepository.UserSearchJpaRepository
import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Repository

@Repository
internal class UserSearchRepositoryImpl(
    private val userSearchJpaRepository: UserSearchJpaRepository
):UserSearchRepository {
    override fun appendHistory(userId: String, keyword: String) {
        userSearchJpaRepository.save(UserSearchJpaEntity.fromFriendSearch(userId, keyword))
    }

    override fun readSearchHistory(userId: String): List<UserSearch> {
        return userSearchJpaRepository.findAllByUserId(userId).map {
            it.toFriendSearch()
        }
    }
}