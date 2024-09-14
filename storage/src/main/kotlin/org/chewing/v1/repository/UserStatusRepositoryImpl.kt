package org.chewing.v1.repository

import org.chewing.v1.jparepository.UserStatusJpaRepository
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
class UserStatusRepositoryImpl(
    private val userStatusJpaRepository: UserStatusJpaRepository
) : UserStatusRepository {
    override fun readUserStatuses(userId: User.UserId): List<User.UserStatus> {
        val userStatusEntity = userStatusJpaRepository.findAllByUserId(userId.value())
        return userStatusEntity.map { it.toUserStatus() }
    }

    override fun removeUserStatus(statusId: String) {
        userStatusJpaRepository.deleteById(statusId)
    }
}