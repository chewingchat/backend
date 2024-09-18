package org.chewing.v1.dto.request

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedTarget

class LikesRequest {
    data class Add(
        val feedId: String = ""
    ) {
        fun toFeedId(): Feed.FeedId {
            return Feed.FeedId.of(feedId)
        }
        fun toTarget(): FeedTarget {
            return FeedTarget.LIKES
        }
    }

    data class Delete(
        val feedId: String = ""
    ) {
        fun toFeedId(): Feed.FeedId {
            return Feed.FeedId.of(feedId)
        }
        fun toUpdateType(): FeedTarget {
            return FeedTarget.UNLIKES
        }
    }
}