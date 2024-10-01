package org.chewing.v1.model.media

import java.util.*


class Media private constructor(
    val category: FileCategory,
    val url: String,
    val type: MediaType,
    val index: Int,
    val isBasic: Boolean
)  {
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun upload(category: FileCategory, userId: String, fileName: String, type: MediaType): Media {
            val randomId = UUID.randomUUID().toString()
            val path = "$DEFAULT_IMAGE_URL/{${category.name}/$userId/$randomId/$fileName"
            return Media.of(category, path, fileName.split(".")[0].toInt(), type)
        }

        fun of(category: FileCategory, imagePath: String, index: Int, type: MediaType): Media =
            Media.of(category, imagePath, index, type)
    }

    fun isBasic(): Boolean {
        return type == MediaType.IMAGE_BASIC
    }
}
