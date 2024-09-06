package org.chewing.v1.dto.request

import org.chewing.v1.model.feed.Feed

class LikesRequest {
    data class AddLikesRequest(
        val feedId: String
    ) {
        fun toFeedId(): Feed.FeedId {
            return Feed.FeedId.of(feedId)
        }
    }

    data class DeleteLikesRequest(
        val feedId: String
    ) {
        fun toFeedId(): Feed.FeedId {
            return Feed.FeedId.of(feedId)
        }
    }
}