package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.feed.FulledFeed
import java.time.format.DateTimeFormatter

data class UserFeedResponse(
    val feedId: String,
    val totalLiked: Int,
    val feedUploadTime: String,
    val feedTopic: String,
    val totalComment: Int,
    val feedDetails: List<FeedDetailResponse>
) {
    companion object {
        fun of(
            fulledFeed: FulledFeed
        ): UserFeedResponse {
            val formattedUploadTime = fulledFeed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return UserFeedResponse(
                feedId = fulledFeed.feed.id.value(),
                totalLiked = fulledFeed.feed.likes,
                feedUploadTime = formattedUploadTime,
                feedTopic = fulledFeed.feed.topic,
                totalComment = fulledFeed.feed.comments,
                feedDetails = fulledFeed.details.map { FeedDetailResponse.of(it) }
            )
        }
    }
}