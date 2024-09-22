package org.chewing.v1.implementation.user

import org.chewing.v1.model.PushToken
import org.chewing.v1.model.User
import org.chewing.v1.model.UserContent
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository
) {

    fun appendUserPushToken(user: User, appToken: String, device: PushToken.Device) {
        userRepository.appendPushToken(device, appToken, user)
    }

    fun appendUser(user: UserContent): User {
        return userRepository.appendUser(user)
    }

    fun appendSearched(user: User, search: FriendSearch) {
        return userRepository.appendSearchHistory(user, search)
    }
}