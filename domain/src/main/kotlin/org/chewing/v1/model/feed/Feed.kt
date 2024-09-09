package org.chewing.v1.model.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedDetail
import java.io.Writer
import java.time.LocalDateTime

class Feed private constructor(
    val feedId: FeedId,
    val feedTopic: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val feedUploadTime: LocalDateTime,
    val feedDetails: List<FeedDetail>,
    val writer: User,
    val version: Long = 0,
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
            comments: Int,
            feedUploadTime: LocalDateTime,
            feedDetails: List<FeedDetail>,
            writer: User,
            version: Long
        ): Feed {
            return Feed(FeedId.of(feedId), feedTopic, likes, comments, feedUploadTime, feedDetails, writer, version)
        }
    }

    fun appendLikes(): Feed {
        return Feed(
            FeedId.of(feedId.value()),
            feedTopic,
            likes + 1,
            comments,
            feedUploadTime,
            feedDetails,
            writer,
            version
        )
    }

    fun removeLikes(): Feed {
        return Feed(
            FeedId.of(feedId.value()),
            feedTopic,
            likes - 1,
            comments,
            feedUploadTime,
            feedDetails,
            writer,
            version
        )
    }

    fun appendComments(): Feed {
        return Feed(
            FeedId.of(feedId.value()),
            feedTopic,
            likes,
            comments + 1,
            feedUploadTime,
            feedDetails,
            writer,
            version
        )
    }
    fun removeComments(): Feed {
        return Feed(
            FeedId.of(feedId.value()),
            feedTopic,
            likes,
            comments - 1,
            feedUploadTime,
            feedDetails,
            writer,
            version
        )
    }
}