package org.chewing.v1.model.media

import java.util.*

class Media private constructor(
    val category: FileCategory,
    val url: String,
    val type: MediaType,
    val index: Int,
) {
    companion object {
        fun upload(mediaUrl: String, category: FileCategory, userId: String, fileName: String, type: MediaType): Media {
            val randomId = UUID.randomUUID().toString()
            val path = "$mediaUrl/${category.name}/$userId/$randomId/$fileName"
            return Media(category, path, type, fileName.split(".")[0].toInt())
        }

        fun of(category: FileCategory, imagePath: String, index: Int, type: MediaType): Media = Media(category, imagePath, type, index)
    }
}
