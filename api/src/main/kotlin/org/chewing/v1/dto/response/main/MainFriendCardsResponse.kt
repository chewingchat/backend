package org.chewing.v1.dto.response.main

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.friend.Friend

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
        val friendActivate: String,
        val isFavorite: Boolean,
        val friendStatusMessage: String,
        val friendStatusEmoticon: String,
    ) {
        companion object {
            fun of(friend: Friend): FriendCardResponse {
                return FriendCardResponse(
                    friendId = friend.friend.userId,
                    friendFirstName = friend.name.firstName(),
                    friendLastName = friend.name.lastName(),
                    friendImageUrl = friend.friend.image.url,
                    friendStatusMessage = friend.friendStatus.statusMessage,
                    isFavorite = friend.isFavorite,
                    friendActivate = friend.friend.type.name,
                    friendBackgroundImageUrl = friend.friend.backgroundImage.url,
                    friendStatusEmoticon = friend.friendStatus.emoticon.media.url
                )
            }
        }
    }

    companion object {
        fun ofList(user: User, userStatus: UserStatus, friends: List<Friend>): MainFriendCardsResponse {
            return MainFriendCardsResponse(
                friends = friends.map { FriendCardResponse.of(it) },
                userStatusMessage = userStatus.statusMessage,
                userImageUrl = user.image.url,
                userFirstName = user.name.firstName(),
                userLastName = user.name.lastName(),
                totalFriends = friends.size,
                userStatusEmoticon = userStatus.emoticon.media.url
            )
        }
    }
}
