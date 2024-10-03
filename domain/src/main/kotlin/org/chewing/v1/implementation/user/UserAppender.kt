package org.chewing.v1.implementation.user

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.repository.PushNotificationRepository
import org.chewing.v1.repository.UserRepository
import org.chewing.v1.repository.UserSearchRepository
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository,
    private val userStatusRepository: UserStatusRepository,
    private val pushNotificationRepository: PushNotificationRepository,
    private val userSearchRepository: UserSearchRepository
) {

    fun appendUserPushToken(user: User, appToken: String, device: PushToken.Device) {
        pushNotificationRepository.append(device, appToken, user)
    }

    fun appendIfNotExist(contact: Contact): User {
        return userRepository.append(contact)
    }

    fun appendSearched(user: User, keyword: String) {
        return userSearchRepository.appendSearchHistory(user, keyword)
    }

    fun appendStatus(userId: String, statusMessage: String, emoji: String) {
        userStatusRepository.append(userId, statusMessage, emoji)
    }
}