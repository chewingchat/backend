package org.chewing.v1.implementation.user

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import org.chewing.v1.repository.PushNotificationRepository
import org.chewing.v1.repository.UserRepository
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserRemover(
    private val userRepository: UserRepository,
    private val pushNotificationRepository: PushNotificationRepository
) {
    @Transactional
    fun remove(userId: String): User {
        return userRepository.remove(userId) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    @Transactional
    fun removePushToken(device: PushToken.Device) {
        pushNotificationRepository.remove(device)
    }
}