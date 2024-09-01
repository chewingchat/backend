package org.chewing.v1.repository

import org.chewing.v1.jpaentity.FriendSearchJpaEntity
import org.chewing.v1.jpaentity.UserJpaEntity
import org.chewing.v1.jparepository.AuthJpaRepository
import org.chewing.v1.jparepository.FriendSearchJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.model.FriendSearch
import org.chewing.v1.model.User
import org.springframework.core.annotation.MergedAnnotations.Search
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val authJpaRepository: AuthJpaRepository,
    private val friendSearchJpaRepository: FriendSearchJpaRepository
) : UserRepository {
    override fun readUserById(userId: User.UserId): User? {
        val userEntity = userJpaRepository.findById(userId.value())
        return userEntity.map { it.toUser() }.orElse(null)
    }


    override fun remove(userId: User.UserId): User.UserId? {
        userJpaRepository.deleteById(userId.value())
        return userId
    }

    override fun updateUser(user: User): User.UserId? {
        userJpaRepository.save(UserJpaEntity.fromUser(user))
        return user.userId
    }

    override fun readUserByEmail(email: String): User? {
        return authJpaRepository.findByEmail(email).map {
            it.user.toUser()
        }.orElse(null)
    }

    override fun readUserByPhoneNumber(email: String): User? {
        return authJpaRepository.findByEmail(email).map {
            it.user.toUser()
        }.orElse(null)
    }

    override fun appendSearchHistory(user: User, search: FriendSearch) {
        friendSearchJpaRepository.save(FriendSearchJpaEntity.fromFriendSearch(user, search))
    }

    override fun readSearchHistory(userId: User.UserId): List<FriendSearch> {
        return friendSearchJpaRepository.findAllByUserId(userId.value()).map {
            it.toFriendSearch()
        }
    }
}