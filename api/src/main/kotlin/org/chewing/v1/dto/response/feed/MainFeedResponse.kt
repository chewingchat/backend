package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.feed.FriendFeed
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
                friendFeed.fulledFeed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return MainFeedResponse(
                feedId = friendFeed.fulledFeed.feed.id.value(),
                isLiked = friendFeed.isLiked,
                totalLiked = friendFeed.fulledFeed.feed.likes,
                feedUploadTime = formattedUploadTime,
                feedMainDetailFileUrl = friendFeed.fulledFeed.details[0].media.url,
                type = friendFeed.fulledFeed.details[0].media.type.toString().lowercase()
            )
        }
    }
}