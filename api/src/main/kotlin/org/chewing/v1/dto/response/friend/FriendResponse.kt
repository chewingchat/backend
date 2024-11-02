package org.chewing.v1.dto.response.friend

import org.chewing.v1.model.friend.Friend

data class FriendResponse(
    val friendId: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val imageType: String,
    val access: String,
    val favorite: Boolean,
    val statusMessage: String,
    val statusEmoji: String,
) {
    companion object {
        fun from(friend: Friend): FriendResponse {
            return FriendResponse(
                friendId = friend.user.userId,
                firstName = friend.name.firstName(),
                lastName = friend.name.lastName(),
                imageUrl = friend.user.image.url,
                access = friend.user.status.name.lowercase(),
                statusMessage = friend.status.message,
                favorite = friend.isFavorite,
                statusEmoji = friend.status.emoji,
                imageType = friend.user.image.type.value().lowercase(),
            )
        }
    }
}