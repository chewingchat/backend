package org.chewing.v1.dto.request

import org.chewing.v1.model.feed.Feed

class FeedRequest {
    data class Delete(
        val feedId: String = ""
    ) {
        fun toFeedId(): Feed.FeedId {
            return Feed.FeedId.of(feedId)
        }
    }
}