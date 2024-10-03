package org.chewing.v1.external

import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class ExternalImageClientImpl : ExternalImageClient {

    override fun uploadFile(file: FileData, media: Media): String {
        return "1"
    }

    override fun removeFile(media: Media): String {
        return "1"
    }
}