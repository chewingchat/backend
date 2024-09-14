package org.chewing.v1.model.media

import java.util.*

class Image private constructor(
    override val url: String,
    override val index: Int
) : Media {
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun upload(category: ImageCategory, userId: String, fileName: String): Image {
            val randomId = UUID.randomUUID().toString()
            val path = "$DEFAULT_IMAGE_URL/{${category.name}/$userId/$randomId/$fileName"
            return Image(path, fileName.split(".")[0].toInt())
        }

        fun of(imagePath: String, index: Int): Image = Image(imagePath, index)

        fun empty(): Image = Image("", 0)
    }

    enum class ImageCategory {
        USER_PROFILE,
        FEED,
        EMOTICON
    }

    override val type: MediaType
        get() = MediaType.IMAGE
    override val isEmpty: Boolean
        get() = url.isEmpty()
}
