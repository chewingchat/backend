package org.chewing.v1.dto.response.friend

import org.chewing.v1.dto.response.feed.MainFeedResponse
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendFeed

data class FriendDetailResponse(
    val feeds: List<MainFeedResponse>
) {
    companion object {
        fun of(
            friendFeeds: List<FriendFeed>
        ): FriendDetailResponse {
            return FriendDetailResponse(
                feeds = friendFeeds.map {
                    MainFeedResponse.of(it)
                }
            )
        }
    }
}