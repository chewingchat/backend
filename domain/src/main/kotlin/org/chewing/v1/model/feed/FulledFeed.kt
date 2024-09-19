package org.chewing.v1.model.feed

import org.chewing.v1.model.User

class FulledFeed private constructor(
    val feed: Feed,
    val details: List<FeedDetail>,
) {
    companion object {
        fun of(
            feed: Feed,
            details: List<FeedDetail>,
        ): FulledFeed {
            return FulledFeed(feed, details)
        }
    }
}