package org.chewing.v1.model.media

import org.chewing.v1.model.User
import java.util.*

class Video private constructor(
    private val videoUrl: String,
    override val index: Int
) : Media {
    companion object {
        private const val DEFAULT_VIDEO_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun upload(category: VideoCategory, userId: String, fileName: String): Video {
            val randomId = UUID.randomUUID().toString()
            val path = "${DEFAULT_VIDEO_URL}/{${category.name}/$userId/$randomId/$fileName"
            return Video(path, fileName.split(".")[0].toInt())
        }

        fun of(imagePath: String, index: Int): Video = Video(imagePath, index)
    }

    enum class VideoCategory {
        FEED,
        EMOTICON
    }

    override val url: String get() = videoUrl
    override val type: MediaType
        get() = MediaType.VIDEO
}
