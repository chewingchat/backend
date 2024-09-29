package org.chewing.v1.implementation.user

import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StatusUpdater(
    private val statusRepository: UserStatusRepository
) {
    @Transactional
    fun updateSelectedStatusTrue(userId: String, statusId: String) {
        statusRepository.updateSelectedStatusFalse(userId)
        statusRepository.updateSelectedStatusTrue(userId, statusId)
    }
    @Transactional
    fun updateDeselectedStatusFalse(userId: String) {
        statusRepository.updateSelectedStatusFalse(userId)
    }
}