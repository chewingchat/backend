package org.chewing.v1.model.feed

import org.chewing.v1.model.media.Media

class FeedDetail private constructor(
    val feedDetailId: String,
    val index: Int,
    val media: Media
) {
    companion object {
        fun of(feedDetailId: String, index: Int, media: Media): FeedDetail {
            return FeedDetail(feedDetailId, index, media)
        }
    }
}