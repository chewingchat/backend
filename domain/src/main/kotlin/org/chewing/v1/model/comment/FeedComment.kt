package org.chewing.v1.model.comment

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FulledFeed

class FeedComment(
    val feed: FulledFeed,
    val comment: Comment
) {
    companion object {
        fun of(feed: FulledFeed, comment: Comment): FeedComment {
            return FeedComment(
                feed = feed,
                comment = comment
            )
        }
    }
}