package org.chewing.v1.implementation.friend

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendUpdater(
    private val friendRepository: FriendRepository
) {
    fun updateFriend(user: User, friend: Friend) {
        friendRepository.updateFriend(user, friend)
    }

    fun updateFavorite(user: User, friendId: User.UserId, favorite: Boolean) {
        friendRepository.updateFavorite(user, friendId, favorite)
    }
    fun updateName(user: User, friendId: User.UserId, friendName: User.UserName) {
        friendRepository.updateName(user, friendId, friendName)
    }
}