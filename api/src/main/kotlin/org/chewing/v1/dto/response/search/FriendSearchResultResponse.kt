package org.chewing.v1.dto.response.search

import org.chewing.v1.dto.response.friend.FriendResponse
import org.chewing.v1.model.friend.Friend

data class FriendSearchResultResponse(
    val friends: List<FriendResponse>,
    val totalFriends: Int
) {
    companion object {
        fun ofList(friends: List<Friend>): FriendSearchResultResponse {
            return FriendSearchResultResponse(
                friends = friends.map { FriendResponse.of(it) },
                totalFriends = friends.size
            )
        }
    }
}