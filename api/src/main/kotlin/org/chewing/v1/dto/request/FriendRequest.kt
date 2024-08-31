package org.chewing.v1.dto.request

import org.chewing.v1.model.User

class FriendRequest(
) {
    data class AddWithEmail(
        val email:String,
        val firstName: String,
        val lastName: String,
    ) {
        fun toUserName(): User.UserName {
            return User.UserName.of(firstName, lastName)
        }
    }
    data class UpdateName(
        val friendId: String,
        val firstName: String,
        val lastName: String
    ) {
        fun toFriendName(): User.UserName {
            return User.UserName.of(firstName, lastName)
        }
        fun toFriendId(): User.UserId {
            return User.UserId.of(friendId)
        }
    }
    data class UpdateFavorite(
        val friendId: String,
        val favorite: Boolean
    )
    data class Delete(
        val friendId: String
    )
    data class AddWithPhone(
        val phone: String,
        val firstName: String,
        val lastName: String,
    ) {
        fun toUserName(): User.UserName {
            return User.UserName.of(firstName, lastName)
        }
    }
}