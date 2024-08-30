package org.chewing.v1.model

class Friend private constructor(
    val friend: User,
    val favorite: Boolean,
    val friendName: String
) {
    companion object {
        fun of(friend: User, favorite: Boolean, friendName: String): Friend {
            return Friend(
                friend = friend,
                favorite = favorite,
                friendName = friendName
            )
        }

        fun generate(friend: User, friendName: String): Friend {
            return Friend(
                friend = friend,
                favorite = false,
                friendName = friendName
            )
        }
    }

    fun updateFavorite(favorite: Boolean): Friend {
        return Friend(
            friend = this.friend,
            favorite = favorite,
            friendName = this.friendName
        )
    }
}