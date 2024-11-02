package org.chewing.v1.dto.response.main

import org.chewing.v1.dto.response.friend.FriendResponse
import org.chewing.v1.dto.response.user.UserResponse
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.friend.Friend

data class MainFriendListResponse(
    val friends: List<FriendResponse>,
    val user: UserResponse,
    val totalFriends: Int
) {

    companion object {
        fun ofList(user: User, userStatus: UserStatus, friends: List<Friend>): MainFriendListResponse {
            return MainFriendListResponse(
                friends = friends.map { FriendResponse.from(it) },
                user = UserResponse.of(user, userStatus),
                totalFriends = friends.size
            )
        }
    }
}
