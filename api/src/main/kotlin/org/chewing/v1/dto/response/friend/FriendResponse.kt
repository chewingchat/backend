package org.chewing.v1.dto.response.friend

import org.chewing.v1.model.friend.Friend

data class FriendResponse(
    val friendId: String,
    val friendFirstName: String,
    val friendLastName: String,
    val friendImageUrl: String,
    val isFavorite: Boolean,
    val friendStatusMessage: String,
    val friendStatusEmoticon: String,
) {
    companion object {
        fun of(friend: Friend): FriendResponse {
            return FriendResponse(
                friendId = friend.friend.userId.value(),
                friendFirstName = friend.name.firstName(),
                friendLastName = friend.name.lastName(),
                friendImageUrl = friend.friend.image.url,
                friendStatusMessage = friend.friend.status.statusMessage,
                isFavorite = friend.isFavorite,
                friendStatusEmoticon = friend.friend.status.emoticon.media.url
            )
        }
    }
}