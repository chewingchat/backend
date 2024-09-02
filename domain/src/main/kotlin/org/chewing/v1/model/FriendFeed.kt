package org.chewing.v1.model

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