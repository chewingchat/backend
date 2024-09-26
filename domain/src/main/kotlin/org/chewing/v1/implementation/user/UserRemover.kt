package org.chewing.v1.implementation.user

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRemover(
    private val userRepository: UserRepository,
) {
    fun remove(userId: String) {
        userRepository.remove(userId)
    }
    fun removePushToken(device: PushToken.Device) {
        userRepository.removePushToken(device)
    }
}