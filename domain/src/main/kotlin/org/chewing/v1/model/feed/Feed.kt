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
    val writer: User,
    val version: Long = 0,
    val isFeedOwner: Boolean = false,
    val isLiked: Boolean = false,
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
            writer: User,
            version: Long
        ): Feed {
            return Feed(FeedId.of(feedId), feedTopic, likes, feedUploadTime, feedDetails, writer, version)
        }
    }

    fun appendLikes(): Feed {
        return Feed(FeedId.of(feedId.value()), feedTopic, likes + 1, feedUploadTime, feedDetails, writer, version)
    }

    fun removeLikes(): Feed {
        return Feed(FeedId.of(feedId.value()), feedTopic, likes - 1, feedUploadTime, feedDetails, writer, version)
    }

    fun updateFeedRelation(userId: User.UserId, isLiked: Boolean): Feed {
        return Feed(FeedId.of(feedId.value()), feedTopic, likes, feedUploadTime, feedDetails, writer, version, userId.value().equals(writer.userId.value()), isLiked)
    }
}