package org.chewing.v1.implementation.friend

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendAppender(
    private val friendRepository: FriendRepository
) {
    fun appendFriend(user: User, friendName: UserName, targetUser: User) {
        friendRepository.appendFriend(user, friendName,targetUser)
    }
}