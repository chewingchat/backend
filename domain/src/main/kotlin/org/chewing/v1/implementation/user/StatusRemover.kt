package org.chewing.v1.implementation.user

import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StatusRemover(
    private val statusRepository: UserStatusRepository
) {
    @Transactional
    fun removeAll(userId: String){
        statusRepository.removeByUserId(userId)
    }
}