package org.chewing.v1.dto.request

import org.chewing.v1.model.user.UserName

class UserRequest {
    data class UpdateName(
        val firstName: String,
        val lastName: String
    ){
        fun toUserName(): UserName = UserName.of(firstName, lastName)
    }
}