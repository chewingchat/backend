package org.chewing.v1.model.media

import java.util.*

class Video private constructor(
    override val url: String,
    override val index: Int,
    override val type: MediaType
) : Media {
    companion object {
        private const val DEFAULT_VIDEO_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"
        fun upload(category: FileCategory, userId: String, fileName: String, type: MediaType): Video {
            val randomId = UUID.randomUUID().toString()
            val path = "${DEFAULT_VIDEO_URL}/{${category.name}/$userId/$randomId/$fileName"
            return Video(path, fileName.split(".")[0].toInt(), type)
        }
        fun of(imagePath: String, index: Int, type: MediaType): Video = Video(imagePath, index, type)
    }

    override val isBasic: Boolean
        get() = type == MediaType.VIDEO_MP4
}