package org.chewing.v1.model.feed

import org.chewing.v1.model.media.Media

class FeedDetail private constructor(
    val feedDetailId: String,
    val media: Media
) {
    companion object {
        fun of(feedDetailId: String, media: Media): FeedDetail {
            return FeedDetail(feedDetailId, media)
        }

        fun generate(media: Media): FeedDetail {
            return FeedDetail("", media)
        }
    }
}