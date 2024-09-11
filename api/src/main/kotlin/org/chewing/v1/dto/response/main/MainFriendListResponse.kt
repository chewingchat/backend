package org.chewing.v1.dto.response.main

import org.chewing.v1.dto.response.friend.FriendResponse
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User

data class MainFriendListResponse(
    val friends: List<FriendResponse>,
    val userStatusMessage: String,
    val userImageUrl: String,
    val userFirstName: String,
    val userLastName: String,
    val totalFriends: Int
) {

    companion object {
        fun ofList(user: User, friends: List<Friend>): MainFriendListResponse {
            return MainFriendListResponse(
                friends = friends.map { FriendResponse.of(it) },
                userStatusMessage = user.status.statusMessage,
                userImageUrl = user.image.url,
                userFirstName = user.name.firstName(),
                userLastName = user.name.lastName(),
                totalFriends = friends.size
            )
        }
    }
}