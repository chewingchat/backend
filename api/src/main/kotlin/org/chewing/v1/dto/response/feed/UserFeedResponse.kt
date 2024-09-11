package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.feed.Feed
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
            feed: Feed
        ): UserFeedResponse {
            val formattedUploadTime = feed.feedUploadTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return UserFeedResponse(
                feedId = feed.feedId.value(),
                totalLiked = feed.likes,
                feedUploadTime = formattedUploadTime,
                feedTopic = feed.feedTopic,
                totalComment = feed.comments,
                feedDetails = feed.feedDetails.map { FeedDetailResponse.of(it) }
            )
        }
    }
}