package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.feed.Feed
import java.time.format.DateTimeFormatter

data class FriendFeedResponse(
    val feedId: String,
    val totalLiked: Int,
    val feedUploadTime: String,
    val feedTopic: String,
    val feedDetails: List<FeedDetailResponse>
) {
    companion object {
        fun of(
            feed: Feed
        ): FriendFeedResponse {
            val formattedUploadTime = feed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return FriendFeedResponse(
                feedId = feed.feed.feedId,
                totalLiked = feed.feed.likes,
                feedUploadTime = formattedUploadTime,
                feedTopic = feed.feed.topic,
                feedDetails = feed.feedDetails.map { FeedDetailResponse.of(it) }
            )
        }
    }
}
