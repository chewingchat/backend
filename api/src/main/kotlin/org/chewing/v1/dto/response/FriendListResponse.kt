package org.chewing.v1.dto.response

import org.chewing.v1.model.Friend
import org.chewing.v1.model.User

data class FriendListResponse(
    val friends: List<FriendResponse>,
    val userStatusMessage: String,
    val userImageUrl: String,
    val userFirstName: String,
    val userLastName: String,
    val totalFriends: Int
) {

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
