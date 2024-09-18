package org.chewing.v1.implementation.friend

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendAppender(
    private val friendRepository: FriendRepository
) {
    fun appendFriend(user:User, friendName: User.UserName, targetUser: User) {
        friendRepository.appendFriend(user, friendName,targetUser)
    }
}