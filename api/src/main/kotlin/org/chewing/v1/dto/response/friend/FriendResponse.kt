package org.chewing.v1.dto.response.friend

import org.chewing.v1.model.friend.Friend

data class FriendResponse(
    val friendId: String,
    val friendFirstName: String,
    val friendLastName: String,
    val friendImageUrl: String,
    val friendActivate: String,
    val isFavorite: Boolean,
    val friendStatusMessage: String,
    val friendStatusEmoticon: String,
) {
    companion object {
        fun of(friend: Friend): FriendResponse {
            return FriendResponse(
                friendId = friend.friend.userId,
                friendFirstName = friend.name.firstName(),
                friendLastName = friend.name.lastName(),
                friendImageUrl = friend.friend.image.url,
                friendActivate = friend.friend.type.name,
                friendStatusMessage = friend.friendStatus.statusMessage,
                isFavorite = friend.isFavorite,
                friendStatusEmoticon = friend.friendStatus.emoticon.media.url
            )
        }
    }
}