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
            if (file.name.endsWith(".mp4")) {
                Pair(file, Video.upload(category, userId, file.name, file.contentType))
            } else if (
                file.name.endsWith(".jpg") ||
                file.name.endsWith(".jpeg") ||
                file.name.endsWith(".png")
            ) {
                Pair(file, Image.upload(category, userId, file.name, file.contentType))
            } else {
                throw IllegalArgumentException(
                    "Unsupported file type"
                )
            }
        }
    }

    fun generateMedia(
        file: FileData,
        userId: String,
        category: FileCategory
    ): Media {
        return if (file.name.endsWith(".mp4")) {
            Video.upload(category, userId, file.name, file.contentType)
        } else if (
            file.name.endsWith(".jpg") ||
            file.name.endsWith(".jpeg") ||
            file.name.endsWith(".png")
        ) {
            Image.upload(category, userId, file.name, file.contentType)
        } else {
            throw IllegalArgumentException(
                "Unsupported file type"
            )
        }
    }
}