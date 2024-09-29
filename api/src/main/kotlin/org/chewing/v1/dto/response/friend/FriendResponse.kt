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
    val statusEmoticon: String,
) {
    companion object {
        fun of(friend: Friend): FriendResponse {
            return FriendResponse(
                friendId = friend.user.userId,
                firstName = friend.name.firstName(),
                lastName = friend.name.lastName(),
                imageUrl = friend.user.image.url,
                access = friend.user.type.name.lowercase(),
                statusMessage = friend.status.message,
                favorite = friend.isFavorite,
                statusEmoticon = friend.status.emoticon.media.url,
                imageType = friend.user.image.type.toString().lowercase(),
            )
        }
    }
}