package org.chewing.v1.dto.response.friend

import org.chewing.v1.model.user.User

data class FriendInfoResponse(
    val friendId: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val access: String,
) {
    companion object {
        fun of(
            user: User,
        ): FriendInfoResponse {
            return FriendInfoResponse(
                friendId = user.userId,
                firstName = user.name.firstName(),
                lastName = user.name.lastName(),
                imageUrl = user.image.url,
                access = user.type.name.lowercase(),
            )
        }
    }
}