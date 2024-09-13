package org.chewing.v1.dto.response

import org.chewing.v1.model.User

class UserResponse(
    val userId: String,
    val userName: String,
    val userImageUrl: String,
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                userId = user.userId.value(),
                userName = user.name,
                userImageUrl = user.image.value(),
            )
        }
    }
}