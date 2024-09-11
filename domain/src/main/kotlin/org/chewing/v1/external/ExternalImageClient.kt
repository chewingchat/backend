package org.chewing.v1.external

import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository
import java.io.File

@Repository
interface ExternalImageClient {
    fun uploadFile(file: File, media: Media): String
    fun removeFile(media: Media): String
}