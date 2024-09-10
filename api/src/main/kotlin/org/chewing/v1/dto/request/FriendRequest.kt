package org.chewing.v1.dto.request

import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone

class FriendRequest(
) {
    data class AddWithEmail(
        val email: String,
        val firstName: String,
        val lastName: String
    ) {
        fun toUserName(): User.UserName = User.UserName.of(firstName, lastName)
        fun toContact(): Email = Email.of(email)
    }

    data class UpdateName(
        val friendId: String,
        val firstName: String,
        val lastName: String
    ) {
        fun toFriendName(): User.UserName = User.UserName.of(firstName, lastName)
        fun toFriendId(): User.UserId = User.UserId.of(friendId)
    }

    data class UpdateFavorite(
        val friendId: String,
        val favorite: Boolean
    )

    data class Delete(
        val friendId: String = ""
    )

    data class AddWithPhone(
        val countyCode: String,
        val phone: String,
        val firstName: String,
        val lastName: String,
    ) {
        fun toUserName(): User.UserName {
            return User.UserName.of(firstName, lastName)
        }

        fun toContact(): Contact {
            return Phone.of(countyCode, phone)
        }
    }
}