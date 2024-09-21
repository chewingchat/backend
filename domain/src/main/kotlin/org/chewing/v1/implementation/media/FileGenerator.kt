package org.chewing.v1.implementation.media

import org.chewing.v1.model.User
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.Video
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileGenerator {
    fun generateFeedMedias(
        files: List<File>,
        userId: String
    ): List<Pair<File, Media>> {
        return files.map { file ->
            if (file.name.endsWith(".mp4")) {
                Pair(file, Video.upload(Video.VideoCategory.FEED, userId, file.name))
            } else if (
                file.name.endsWith(".jpg") ||
                file.name.endsWith(".jpeg") ||
                file.name.endsWith(".png")
            ) {
                Pair(file, Image.upload(Image.ImageCategory.FEED, userId, file.name))
            } else {
                throw IllegalArgumentException(
                    "Unsupported file type"
                )
            }
        }
    }
    fun generateFeedMedia(
        file: File,
        userId: String
    ): Media {
        return if (file.name.endsWith(".mp4")) {
            Video.upload(Video.VideoCategory.FEED, userId, file.name)
        } else if (
            file.name.endsWith(".jpg") ||
            file.name.endsWith(".jpeg") ||
            file.name.endsWith(".png")
        ) {
            Image.upload(Image.ImageCategory.FEED, userId, file.name)
        } else {
            throw IllegalArgumentException(
                "Unsupported file type"
            )
        }
    }
}