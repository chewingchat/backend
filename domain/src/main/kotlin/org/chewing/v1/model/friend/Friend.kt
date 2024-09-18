package org.chewing.v1.model.friend

import org.chewing.v1.model.User

class Friend private constructor(
    val friend: User,
    val isFavorite: Boolean,
    val name: User.UserName,
) {
    companion object {
        fun of(friend: User, favorite: Boolean, friendFirstName: String, friendLastName: String): Friend {
            return Friend(
                friend = friend,
                isFavorite = favorite,
                name = User.UserName.of(friendFirstName, friendLastName),
            )
        }
    }
    fun updateFriend(friend: User): Friend {
        return Friend(
            friend = friend,
            isFavorite = this.isFavorite,
            name = this.name,
        )
    }
}