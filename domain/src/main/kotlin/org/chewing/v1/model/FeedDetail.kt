package org.chewing.v1.model

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