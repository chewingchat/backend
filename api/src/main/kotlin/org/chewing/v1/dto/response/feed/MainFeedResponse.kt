package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.friend.FriendFeed
import java.time.format.DateTimeFormatter

data class MainFeedResponse(
    val feedId: String,
    val isLiked: Boolean,
    val totalLiked: Int,
    val feedMainDetailFileUrl: String,
    val feedUploadTime: String,
    val type: String
) {
    companion object {
        fun of(
            friendFeed: FriendFeed
        ): MainFeedResponse {
            val formattedUploadTime =
                friendFeed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return MainFeedResponse(
                feedId = friendFeed.feed.id.value(),
                isLiked = friendFeed.isLiked,
                totalLiked = friendFeed.feed.likes,
                feedUploadTime = formattedUploadTime,
                feedMainDetailFileUrl = friendFeed.feed.details[0].media.url,
                type = friendFeed.feed.details[0].media.type.toString().lowercase()
            )
        }
    }
}