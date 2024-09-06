package org.chewing.v1.model.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedDetail
import java.io.Writer
import java.time.LocalDateTime

class Feed private constructor(
    val feedId: FeedId,
    val feedTopic: String,
    val likes: Int,
    val feedUploadTime: LocalDateTime,
    val feedDetails: List<FeedDetail>,
    val writer: User
) {
    class FeedId private constructor(private val feedId: String) {
        fun value(): String {
            return feedId
        }

        companion object {
            fun of(value: String): FeedId {
                return FeedId(value)
            }
        }
    }

    companion object {
        fun of(
            feedId: String,
            feedTopic: String,
            likes: Int,
            feedUploadTime: LocalDateTime,
            feedDetails: List<FeedDetail>,
            writer: User
        ): Feed {
            return Feed(FeedId.of(feedId), feedTopic, likes, feedUploadTime, feedDetails, writer)
        }
    }
}