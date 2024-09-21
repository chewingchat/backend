package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.feed.Feed
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
            feed: FriendFeed
        ): MainFeedResponse {
            val formattedUploadTime =
                feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return MainFeedResponse(
                feedId = feed.feedId,
                isLiked = feed.isLiked,
                totalLiked = feed.likes,
                feedUploadTime = formattedUploadTime,
                feedMainDetailFileUrl = feed.feedDetails[0].media.url,
                type = feed.feedDetails[0].media.type.toString().lowercase()
            )
        }
    }
}