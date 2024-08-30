package org.chewing.v1.repository

import org.chewing.v1.jpaentity.UserJpaEntity
import org.chewing.v1.jparepository.AuthJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val authJpaRepository: AuthJpaRepository
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

    override fun readUserByKeyword(keyword: String): User? {
        return authJpaRepository.findByPhoneNumberOrEmail(keyword).map {
            it.user.toUser()
        }.orElse(null)
    }
}