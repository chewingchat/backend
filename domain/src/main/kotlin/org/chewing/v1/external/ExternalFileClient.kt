package org.chewing.v1.external

import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media

interface ExternalFileClient {
    fun uploadFile(file: FileData, media: Media)
    fun removeFile(media: Media)
}
