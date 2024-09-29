package org.chewing.v1.dto.response.main

import org.chewing.v1.dto.response.user.UserResponse
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.friend.Friend

data class MainFriendCardsResponse(
    val friends: List<FriendCardResponse>,
    val user: UserResponse,
    val totalFriends: Int
) {
    data class FriendCardResponse(
        val friendId: String,
        val firstName: String,
        val lastName: String,
        val backgroundImageUrl: String,
        val backgroundImageType: String,
        val imageUrl: String,
        val imageType: String,
        val access: String,
        val favorite: Boolean,
        val statusMessage: String,
        val statusEmoticon: String,
    ) {
        companion object {
            fun of(friend: Friend): FriendCardResponse {
                return FriendCardResponse(
                    friendId = friend.user.userId,
                    firstName = friend.name.firstName(),
                    lastName = friend.name.lastName(),
                    imageUrl = friend.user.image.url,
                    imageType = friend.user.image.type.toString().lowercase(),
                    statusMessage = friend.status.message,
                    favorite = friend.isFavorite,
                    access = friend.user.type.name.lowercase(),
                    backgroundImageUrl = friend.user.backgroundImage.url,
                    backgroundImageType = friend.user.backgroundImage.type.toString().lowercase(),
                    statusEmoticon = friend.status.emoticon.media.url
                )
            }
        }
    }

    companion object {
        fun ofList(user: User, userStatus: UserStatus, friends: List<Friend>): MainFriendCardsResponse {
            return MainFriendCardsResponse(
                friends = friends.map { FriendCardResponse.of(it) },
                user = UserResponse.of(user, userStatus),
                totalFriends = friends.size,
            )
        }
    }
}
