package org.chewing.v1.model.media

class Image private constructor(private val imageUrl: String) : Media {
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun generate(category: ImageCategory): Image {
            val path = when (category) {
                ImageCategory.USER_PROFILE -> "$DEFAULT_IMAGE_URL/user/${ImageType.BASIC}.png"
                ImageCategory.FEED -> "$DEFAULT_IMAGE_URL/peed/${ImageType.BASIC}.png"
                ImageCategory.EMOTICON -> "$DEFAULT_IMAGE_URL/emoticon/${ImageType.BASIC}.png"
            }
            return Image(path)
        }

        fun upload(category: ImageCategory, userId: String, fileName: String): Image {
            val path = when (category) {
                ImageCategory.USER_PROFILE -> "$DEFAULT_IMAGE_URL/user/$userId/${ImageType.UPLOAD}/$fileName"
                ImageCategory.FEED -> "$DEFAULT_IMAGE_URL/peed/$userId/${ImageType.UPLOAD}/$fileName"
                ImageCategory.EMOTICON -> "$DEFAULT_IMAGE_URL/emoticon/$userId/${ImageType.UPLOAD}/$fileName"
            }
            return Image(path)
        }

        fun of(imagePath: String): Image = Image(imagePath)
    }

    enum class ImageType {
        BASIC,
        UPLOAD
    }

    enum class ImageCategory {
        USER_PROFILE,
        FEED,
        EMOTICON
    }

    override val url: String get() = imageUrl
    override val type: MediaType
        get() = MediaType.IMAGE
}
