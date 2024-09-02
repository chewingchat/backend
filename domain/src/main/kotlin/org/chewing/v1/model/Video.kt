package org.chewing.v1.model

class Video private constructor(private val videoUrl: String) : Media {
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun upload(category: VideoCategory, userId: String, fileName: String): Video {
            val path = when (category) {
                VideoCategory.FEED -> "${DEFAULT_IMAGE_URL}/peed/$userId/${VideoType.UPLOAD}/$fileName"
                VideoCategory.EMOTICON -> "${DEFAULT_IMAGE_URL}/emoticon/$userId/${VideoType.UPLOAD}/$fileName"
            }
            return Video(path)
        }

        fun of(imagePath: String): Video = Video(imagePath)
    }

    enum class VideoType {
        UPLOAD
    }

    enum class VideoCategory {
        FEED,
        EMOTICON
    }

    override val url: String get() = videoUrl
    override val type: MediaType
        get() = MediaType.VIDEO
}
