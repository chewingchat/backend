package org.chewing.v1.implementation.media

import org.chewing.v1.external.ExternalFileClient
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component

@Component
class FileAppender(
    private val externalFileClient: ExternalFileClient,
) {
    suspend fun appendFile(file: FileData, media: Media) {
        externalFileClient.uploadFile(file, media)
    }
}
