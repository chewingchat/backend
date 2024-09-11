package org.chewing.v1.dto.response.feed

import org.chewing.v1.model.feed.FeedDetail

data class FeedDetailResponse(
    val index: Int,
    val feedDetailFileUrl: String,
    val type: String
) {
    companion object {
        fun of(
            feedDetail: FeedDetail
        ): FeedDetailResponse {
            return FeedDetailResponse(feedDetail.media.index, feedDetail.media.url, feedDetail.media.type.toString().lowercase())
        }
    }
}