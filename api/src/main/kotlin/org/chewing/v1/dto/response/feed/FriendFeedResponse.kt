package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.friend.FriendFeed
import java.time.format.DateTimeFormatter

data class FriendFeedResponse(
    val feedId: String,
    val isLiked: Boolean,
    val totalLiked: Int,
    val feedUploadTime: String,
    val feedTopic: String,
    val feedDetails: List<FeedDetailResponse>
) {
    companion object {
        fun of(
            friendFeed: FriendFeed
        ): FriendFeedResponse {
            val formattedUploadTime = friendFeed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return FriendFeedResponse(
                feedId = friendFeed.feed.id.value(),
                isLiked = friendFeed.isLiked,
                totalLiked = friendFeed.feed.likes,
                feedUploadTime = formattedUploadTime,
                feedTopic = friendFeed.feed.topic,
                feedDetails = friendFeed.feed.details.map { FeedDetailResponse.of(it) }
            )
        }
    }
}
