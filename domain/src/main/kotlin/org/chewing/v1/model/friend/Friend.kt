package org.chewing.v1.model.friend

import org.chewing.v1.model.User

class Friend private constructor(
    val friend: User,
    val isFavorite: Boolean,
    val friendName: User.UserName,
) {
    companion object {
        fun of(friend: User, favorite: Boolean, friendFirstName: String, friendLastName: String): Friend {
            return Friend(
                friend = friend,
                isFavorite = favorite,
                friendName = User.UserName.of(friendFirstName, friendLastName),
            )
        }

        fun generate(friend: User, friendName: User.UserName): Friend {
            return Friend(
                friend = friend,
                isFavorite = false,
                friendName = friendName,
            )
        }
    }

    fun updateFavorite(favorite: Boolean): Friend {
        return Friend(
            friend = this.friend,
            isFavorite = favorite,
            friendName = this.friendName,
        )
    }
    fun updateName(friendName: User.UserName): Friend {
        return Friend(
            friend = this.friend,
            isFavorite = this.isFavorite,
            friendName = friendName,
        )
    }
}