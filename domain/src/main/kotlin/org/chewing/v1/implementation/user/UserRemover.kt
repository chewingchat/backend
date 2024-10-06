package org.chewing.v1.implementation.user

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.repository.PushNotificationRepository
import org.chewing.v1.repository.UserRepository
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserRemover(
    private val userRepository: UserRepository,
    private val statusRepository: UserStatusRepository,
    private val pushNotificationRepository: PushNotificationRepository
) {
    @Transactional
    fun remove(userId: String) {
        userRepository.remove(userId)
    }
    @Transactional
    fun removePushToken(device: PushToken.Device) {
        pushNotificationRepository.remove(device)
    }
    @Transactional
    fun removeUserStatuses(userId: String){
        statusRepository.removeAllByUserId(userId)
    }

    @Transactional
    fun removeStatuses(statusesId: List<String>){
        statusRepository.removes(statusesId)
    }
}