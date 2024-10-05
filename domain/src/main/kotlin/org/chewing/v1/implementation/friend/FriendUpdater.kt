package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FriendUpdater(
    private val friendShipRepository: FriendShipRepository
) {
    @Transactional
    fun updateFavorite(userId: String, friendId: String, favorite: Boolean) {
        friendShipRepository.updateFavorite(userId, friendId, favorite) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }

    @Transactional
    fun updateName(userId: String, friendId: String, friendName: UserName) {
        friendShipRepository.updateName(userId, friendId, friendName) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }

    fun updateToUser(user: User, friend: FriendShip): User {
        return User.of(
            userId = user.userId,
            firstName = friend.friendName.firstName,
            lastName = friend.friendName.lastName,
            image = user.image,
            backgroundImage = user.backgroundImage,
            birth = user.birth,
            status = user.status
        )
    }
}