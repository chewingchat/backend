package org.chewing.v1.dto.request

import org.chewing.v1.model.User

data class FriendUpdateWithNameRequest(
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