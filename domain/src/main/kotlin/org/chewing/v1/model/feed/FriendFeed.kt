package org.chewing.v1.model.feed

import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import java.time.LocalDateTime

class FriendFeed private constructor(
    val feedId: String,
    val topic: String,
    val uploadAt: LocalDateTime,
    val isLiked: Boolean,
    val likes: Int,
    val feedDetails: List<FeedDetail>
) {
    companion object {
        fun of(
            feedId: String,
            topic: String,
            uploadAt: LocalDateTime,
            isLiked: Boolean,
            likes: Int,
            feedDetails: List<FeedDetail>
        ): FriendFeed {
            return FriendFeed(feedId, topic, uploadAt, isLiked, likes, feedDetails)
        }
    }
}