package org.chewing.v1.dto.response

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendFeed

data class FriendDetailResponse(
    val friend: FriendResponse,
    val feeds: List<MainFeedResponse>
) {
    companion object {
        fun of(
            friend: Friend,
            friendFeeds: List<FriendFeed>
        ): FriendDetailResponse {
            return FriendDetailResponse(
                friend = FriendResponse.of(friend),
                feeds = friendFeeds.map {
                    MainFeedResponse.of(it)
                }
            )
        }
    }
}