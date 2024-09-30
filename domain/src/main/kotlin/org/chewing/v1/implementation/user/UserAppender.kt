package org.chewing.v1.implementation.user

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.repository.UserRepository
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository,
    private val userStatusRepository: UserStatusRepository
) {

    fun appendUserPushToken(user: User, appToken: String, device: PushToken.Device) {
        userRepository.appendPushToken(device, appToken, user)
    }

    fun appendIfNotExist(contact: Contact): User {
        return userRepository.appendUser(contact)
    }

    fun appendSearched(user: User, search: FriendSearch) {
        return userRepository.appendSearchHistory(user, search)
    }

    fun appendStatus(userId: String, statusMessage: String, emoji: String) {
        userStatusRepository.append(userId, statusMessage, emoji)
    }
}