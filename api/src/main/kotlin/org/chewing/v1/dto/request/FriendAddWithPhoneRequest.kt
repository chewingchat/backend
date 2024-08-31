package org.chewing.v1.dto.request

import org.chewing.v1.model.User

data class FriendAddWithPhoneRequest(
    val phone: String,
    val firstName: String,
    val lastName: String,
) {
    fun toUserName(): User.UserName {
        return User.UserName.of(firstName, lastName)
    }
}