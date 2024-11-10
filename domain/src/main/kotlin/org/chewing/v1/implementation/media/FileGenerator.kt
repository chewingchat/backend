package org.chewing.v1.implementation.media

import org.chewing.v1.model.media.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FileGenerator(
    @Value("\${media.url}") private val mediaUrl: String,
) {
    fun generateMedias(
        files: List<FileData>,
        userId: String,
        category: FileCategory,
    ): List<Pair<FileData, Media>> = files.map { file ->
        Pair(file, Media.upload(mediaUrl, category, userId, file.name, file.contentType))
    }

    fun generateMedia(
        file: FileData,
        userId: String,
        category: FileCategory,
    ): Media = Media.upload(mediaUrl, category, userId, file.name, file.contentType)
}
