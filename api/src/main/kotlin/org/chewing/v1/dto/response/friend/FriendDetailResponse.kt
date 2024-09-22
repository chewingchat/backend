package org.chewing.v1.dto.response.friend

import org.chewing.v1.dto.response.feed.MainFeedResponse
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FriendFeed

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