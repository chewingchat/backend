package org.chewing.v1.implementation.media

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.util.IoScope
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileProcessor(
    private val fileProvider: FileProvider,
    private val fileGenerator: FileGenerator,
    private val fileValidator: FileValidator,
    @IoScope private val ioScope: CoroutineScope
) {
    fun processNewFiles(userId: String, files: List<FileData>, category: FileCategory): List<Media> {
        fileValidator.validateFilesNameCorrect(files)
        val mediaWithFiles = fileGenerator.generateMedias(files, userId, category)
        mediaWithFiles.forEach {
            ioScope.launch {
                val (file, media) = it
                fileProvider.appendFile(file, media)
            }
        }
        return mediaWithFiles.map { it.second }
    }

    fun processNewFile(userId: String, file: FileData, category: FileCategory): Media {
        fileValidator.validateFileNameCorrect(file)
        val media = fileGenerator.generateMedia(file, userId, category)
        fileProvider.appendFile(file, media)
        return media
    }

    fun processOldFile(media: Media) {
        if (media.type != MediaType.IMAGE_BASIC) {
            fileProvider.removeFile(media)
        }
    }

    fun processOldFiles(medias: List<Media>) {
        medias.forEach {
            ioScope.launch {
                fileProvider.removeFile(it)
            }
        }
    }
}