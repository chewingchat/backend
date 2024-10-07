package org.chewing.v1.implementation.friendship

import org.chewing.v1.model.user.UserName
import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component

@Component
class FriendShipAppender(
    private val friendShipRepository: FriendShipRepository,
) {
    fun appendFriend(userId: String, userName: UserName, friendId: String, friendName: UserName) {
        friendShipRepository.append(userId, friendId, friendName)
        friendShipRepository.append(friendId, userId, userName)
    }
}