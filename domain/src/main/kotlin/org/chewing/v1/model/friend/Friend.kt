package org.chewing.v1.model.friend

import org.chewing.v1.model.User
import org.chewing.v1.model.UserName
import org.chewing.v1.model.UserStatus

class Friend private constructor(
    val friend: User,
    val isFavorite: Boolean,
    val name: UserName,
    val friendStatus: UserStatus,
) {
    companion object {
        fun of(friend: User, favorite: Boolean, friendName: UserName, friendStatus: UserStatus): Friend {
            return Friend(
                friend = friend,
                isFavorite = favorite,
                name = friendName,
                friendStatus = friendStatus
            )
        }
    }
}