package org.chewing.v1.dto.request.feed

import org.chewing.v1.model.feed.FeedTarget

class LikesRequest {
    data class Add(
        val feedId: String = ""
    ) {
        fun toFeedId(): String {
            return feedId
        }
        fun toTarget(): FeedTarget {
            return FeedTarget.LIKES
        }
    }

    data class Delete(
        val feedId: String = ""
    ) {
        fun toFeedId(): String {
            return feedId
        }
        fun toUpdateType(): FeedTarget {
            return FeedTarget.UNLIKES
        }
    }
}
