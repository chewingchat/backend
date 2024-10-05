package org.chewing.v1.implementation.friend

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component

@Component
class FriendAppender(
    private val friendShipRepository: FriendShipRepository,
) {
    fun appendFriend(user: User, friendName: UserName, targetUser: User) {
        friendShipRepository.append(user, friendName, targetUser)
        friendShipRepository.append(targetUser, targetUser.name, user)
    }
}