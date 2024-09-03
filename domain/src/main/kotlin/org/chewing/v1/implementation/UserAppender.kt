package org.chewing.v1.implementation

import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository
) {
    fun appendSearchHistory(user: User, search: FriendSearch) {
        return userRepository.appendSearchHistory(user, search)
    }
}