package org.chewing.v1.dto.response

import org.chewing.v1.model.Friend

data class FriendResponse(
    val friendId: String,
    val friendName: String,
    val friendImageUrl: String,
    val friendStatusMessage: String,
    val friendIsFavorite: Boolean,
) {
    companion object {
        private fun of(friend: Friend): FriendResponse {
            return FriendResponse(
                friendId = friend.friend.userId.value(),
                friendName = friend.friendName,
                friendImageUrl = friend.friend.image.value(),
                friendStatusMessage = friend.friend.statusMessage,
                friendIsFavorite = friend.favorite,
            )
        }

        fun ofList(friends: List<Friend>): List<FriendResponse> {
            return friends.map { of(it) }
        }
    }
}