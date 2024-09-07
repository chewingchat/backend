package org.chewing.v1.model.friend

import org.chewing.v1.model.feed.Feed

class FriendFeed(
    val feed: Feed,
    val isLiked: Boolean,
) {
    companion object {
        fun of(feed: Feed, isLiked: Boolean): FriendFeed {
            return FriendFeed(
                feed = feed,
                isLiked = isLiked,
            )
        }
    }
}