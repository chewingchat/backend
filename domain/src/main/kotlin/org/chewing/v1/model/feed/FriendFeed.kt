package org.chewing.v1.model.feed

import org.chewing.v1.model.friend.Friend

class FriendFeed(
    val fulledFeed: FulledFeed,
    val isLiked: Boolean,
    val friend: Friend
) {
    companion object {
        fun of(feed: FulledFeed, isLiked: Boolean,friend: Friend): FriendFeed {
            return FriendFeed(
                fulledFeed = feed,
                isLiked = isLiked,
                friend = friend
            )
        }
    }
}