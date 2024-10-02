package org.chewing.v1.model.media

import java.util.*


class Media private constructor(
    val category: FileCategory,
    val url: String,
    val type: MediaType,
    val index: Int,
) {
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://chewing.s3.ap-northeast-2.amazonaws.com"

        fun upload(category: FileCategory, userId: String, fileName: String, type: MediaType): Media {
            val randomId = UUID.randomUUID().toString()
            val path = "$DEFAULT_IMAGE_URL/{${category.name}/$userId/$randomId/$fileName"
            return Media(category, path, type, fileName.split(".")[0].toInt())
        }

        fun of(category: FileCategory, imagePath: String, index: Int, type: MediaType): Media {
            return Media(category, imagePath, type, index)
        }
    }

}
