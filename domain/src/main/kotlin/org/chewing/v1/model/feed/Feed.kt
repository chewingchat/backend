package org.chewing.v1.model.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.media.Media
import java.time.LocalDateTime

class Feed private constructor(
    val id: FeedId,
    val topic: String,
    val likes: Int,
    val comments: Int,
    val uploadAt: LocalDateTime,
) {
    class FeedId private constructor(private val id: String) {
        fun value(): String {
            return id
        }

        companion object {
            fun of(value: String): FeedId {
                return FeedId(value)
            }
        }
    }

    companion object {
        fun of(
            id: String,
            topic: String,
            likes: Int,
            comments: Int,
            uploadAt: LocalDateTime,
        ): Feed {
            return Feed(FeedId.of(id), topic, likes, comments, uploadAt)
        }
    }
}