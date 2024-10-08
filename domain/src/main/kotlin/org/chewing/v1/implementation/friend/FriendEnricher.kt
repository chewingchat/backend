package org.chewing.v1.implementation.friend

import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class FriendEnricher {

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