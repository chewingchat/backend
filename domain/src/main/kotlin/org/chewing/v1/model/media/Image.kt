package org.chewing.v1.model.media

import java.util.*

class Image private constructor(
    override val category: FileCategory,
    override val url: String,
    override val index: Int,
    override val type: MediaType
) : Media {
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun upload(category: FileCategory, userId: String, fileName: String, type: MediaType): Image {
            val randomId = UUID.randomUUID().toString()
            val path = "$DEFAULT_IMAGE_URL/{${category.name}/$userId/$randomId/$fileName"
            return Image(category, path, fileName.split(".")[0].toInt(), type)
        }

        fun of(category: FileCategory, imagePath: String, index: Int, type: MediaType): Image =
            Image(category, imagePath, index, type)
    }

    override val isBasic: Boolean
        get() = type == MediaType.IMAGE_BASIC
}
