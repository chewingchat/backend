package org.chewing.v1.repository

import org.chewing.v1.jparepository.UserStatusJpaRepository
import org.chewing.v1.model.user.StatusInfo
import org.springframework.stereotype.Repository

@Repository
internal class UserStatusRepositoryImpl(
    private val userStatusJpaRepository: UserStatusJpaRepository,
) : UserStatusRepository {
    override fun readUserStatuses(userId: String): List<StatusInfo> {
        return userStatusJpaRepository.findAllByUserId(userId).map { it.toUserStatusInfo() }
    }

    override fun removeUserStatus(statusId: String) {
        userStatusJpaRepository.deleteById(statusId)
    }

    override fun readSelectedUserStatus(userId: String): StatusInfo {
        return userStatusJpaRepository.findBySelectedTrue().map {
            it.toUserStatusInfo()
        }.orElse(StatusInfo.default())
    }

    override fun readSelectedUsersStatus(userIds: List<String>): List<StatusInfo> {
        return userStatusJpaRepository.findAllBySelectedTrueAndUserIdIn(userIds).map { it.toUserStatusInfo() }
    }

    override fun removeByUserId(userId: String) {
        userStatusJpaRepository.deleteAllByUserId(userId)
    }
}