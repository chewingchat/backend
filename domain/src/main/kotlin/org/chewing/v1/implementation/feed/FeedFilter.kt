package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FriendFeed
import org.chewing.v1.model.feed.FulledFeed
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Component

@Component
class FeedFilter {

    fun filterFriendFeeds(
        feedWithOwnerId: List<Pair<FulledFeed, User.UserId>>,
        friends: List<Friend>
    ): List<Pair<FulledFeed, Friend>> {
        return feedWithOwnerId
            .mapNotNull { (feed, ownerId) ->
                // 친구 목록에서 소유자 ID와 일치하는 친구를 찾음
                friends.find { it.friend.userId == ownerId }?.let { friend ->
                    // 친구와 함께 반환
                    Pair(feed, friend)
                }
            }
    }
}