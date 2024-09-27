package org.chewing.v1.implementation.friend

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.chewing.v1.model.UserName
import org.chewing.v1.model.friend.FriendInfo
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendUpdater(
    private val friendRepository: FriendRepository
) {
    fun updateFavorite(user: User, friendId: String, favorite: Boolean) {
        friendRepository.updateFavorite(user, friendId, favorite)
    }

    fun updateName(user: User, friendId: String, friendName: UserName) {
        friendRepository.updateName(user, friendId, friendName)
    }

    fun updateToUser(user: User, friend: FriendInfo): User {
        return User.of(
            userId = user.userId,
            firstName = friend.friendName.firstName,
            lastName = friend.friendName.lastName,
            image = user.image,
            backgroundImage = user.backgroundImage,
            birth = user.birth,
        )
    }
}