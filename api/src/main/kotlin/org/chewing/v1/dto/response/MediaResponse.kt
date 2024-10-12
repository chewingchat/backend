package org.chewing.v1.dto.response

import org.chewing.v1.model.media.Media

data class MediaResponse(
    val fileType: String,
    val fileUrl: String,
    val index: Int,
) {
    companion object {
        fun from(media: Media): MediaResponse {
            return MediaResponse(
                fileType = media.type.name,
                fileUrl = media.url,
                index = media.index,
            )
        }
    }
}