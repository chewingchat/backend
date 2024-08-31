package org.chewing.v1.dto.response

import org.chewing.v1.model.Friend
import org.chewing.v1.model.User

class FriendListResponse(
    val friends: List<FriendResponse>,
    val userStatusMessage: String,
    val userImageUrl: String,
    val userFirstName: String,
    val userLastName: String,
    val totalFriends: Int
) {
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
                    friendFirstName = friend.friendName.firstName(),
                    friendLastName = friend.friendName.lastName(),
                    friendImageUrl = friend.friend.image.value(),
                    friendStatusMessage = friend.friend.status.statusMessage,
                    isFavorite = friend.isFavorite,
                    friendStatusEmoticon = friend.friend.status.emoticon.emoticonUrl
                )
            }
        }
    }

    companion object {
        fun ofList(user: User, friends: List<Friend>): FriendListResponse {
            return FriendListResponse(
                friends = friends.map { FriendResponse.of(it) },
                userStatusMessage = user.status.statusMessage,
                userImageUrl = user.image.value(),
                userFirstName = user.name.firstName(),
                userLastName = user.name.lastName(),
                totalFriends = friends.size
            )
        }
    }
}
