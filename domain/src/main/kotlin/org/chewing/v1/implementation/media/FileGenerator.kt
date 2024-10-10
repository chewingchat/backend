package org.chewing.v1.implementation.media

import org.chewing.v1.model.media.*
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileGenerator {
    fun generateMedias(
        files: List<FileData>,
        userId: String,
        category: FileCategory
    ): List<Pair<FileData, Media>> {
        return files.map { file ->
            Pair(file, Media.upload(category, userId, file.name, file.contentType))
        }
    }

    fun generateMedia(
        file: FileData,
        userId: String,
        category: FileCategory
    ): Media {
        return Media.upload(category, userId, file.name, file.contentType)
    }
}