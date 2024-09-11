package org.chewing.v1.dto.response.main

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User

data class MainFriendCardsResponse(
    val friends: List<FriendCardResponse>,
    val userStatusMessage: String,
    val userStatusEmoticon: String,
    val userImageUrl: String,
    val userFirstName: String,
    val userLastName: String,
    val totalFriends: Int
) {
    data class FriendCardResponse(
        val friendId: String,
        val friendFirstName: String,
        val friendLastName: String,
        val friendBackgroundImageUrl: String,
        val friendImageUrl: String,
        val isFavorite: Boolean,
        val friendStatusMessage: String,
        val friendStatusEmoticon: String,
    ) {
        companion object {
            fun of(friend: Friend): FriendCardResponse {
                return FriendCardResponse(
                    friendId = friend.friend.userId.value(),
                    friendFirstName = friend.friendName.firstName(),
                    friendLastName = friend.friendName.lastName(),
                    friendImageUrl = friend.friend.image.url,
                    friendStatusMessage = friend.friend.status.statusMessage,
                    isFavorite = friend.isFavorite,
                    friendBackgroundImageUrl = friend.friend.backgroundImage.url,
                    friendStatusEmoticon = friend.friend.status.emoticon.emoticonUrl
                )
            }
        }
    }

    companion object {
        fun ofList(user: User, friends: List<Friend>): MainFriendCardsResponse {
            return MainFriendCardsResponse(
                friends = friends.map { FriendCardResponse.of(it) },
                userStatusMessage = user.status.statusMessage,
                userImageUrl = user.image.url,
                userFirstName = user.name.firstName(),
                userLastName = user.name.lastName(),
                totalFriends = friends.size,
                userStatusEmoticon = user.status.emoticon.emoticonUrl
            )
        }
    }
}
