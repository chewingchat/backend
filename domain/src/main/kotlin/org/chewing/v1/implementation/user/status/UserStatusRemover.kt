package org.chewing.v1.implementation.user.status

import org.chewing.v1.repository.user.UserStatusRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserStatusRemover(
    private val statusRepository: UserStatusRepository
) {

    @Transactional
    fun removeUsers(userId: String) {
        statusRepository.removeAllByUserId(userId)
    }

    @Transactional
    fun removes(statusesId: List<String>) {
        statusRepository.removes(statusesId)
    }
}