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
    val details: List<FeedDetail>,
    val writer: User,
    val version: Long = 0,
) {
    class FeedId private constructor(private val id: String) {
        fun value(): String {
            return id
        }

        companion object {
            fun of(value: String): FeedId {
                return FeedId(value)
            }

            fun empty(): FeedId {
                return FeedId("")
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
            details: List<FeedDetail>,
            writer: User,
            version: Long
        ): Feed {
            return Feed(FeedId.of(id), topic, likes, comments, uploadAt, details, writer, version)
        }

        fun generate(
            topic: String,
            medias: List<Media>,
            writer: User
        ): Feed {
            val feedDetails = medias.map {
                FeedDetail.generate(it)
            }
            return Feed(FeedId.empty(), topic, 0, 0, LocalDateTime.now(), feedDetails, writer)
        }
    }

    fun appendLikes(): Feed {
        return Feed(
            FeedId.of(id.value()),
            topic,
            likes + 1,
            comments,
            uploadAt,
            details,
            writer,
            version
        )
    }

    fun removeLikes(): Feed {
        return Feed(
            FeedId.of(id.value()),
            topic,
            likes - 1,
            comments,
            uploadAt,
            details,
            writer,
            version
        )
    }

    fun appendComments(): Feed {
        return Feed(
            FeedId.of(id.value()),
            topic,
            likes,
            comments + 1,
            uploadAt,
            details,
            writer,
            version
        )
    }

    fun removeComments(): Feed {
        return Feed(
            FeedId.of(id.value()),
            topic,
            likes,
            comments - 1,
            uploadAt,
            details,
            writer,
            version
        )
    }
}