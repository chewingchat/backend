package org.chewing.v1.model.feed

import org.chewing.v1.model.User

class UserFeed(
    val feed: FulledFeed,
    val writer: User
) {
    companion object {
        fun of(feed: FulledFeed, writer: User): UserFeed {
            return UserFeed(
                feed = feed,
                writer = writer
            )
        }
    }
}