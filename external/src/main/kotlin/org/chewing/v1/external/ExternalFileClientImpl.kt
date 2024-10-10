package org.chewing.v1.external

import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component

@Component
class ExternalFileClientImpl : ExternalFileClient {

    override fun uploadFile(file: FileData, media: Media): String {
        return "1"
    }

    override fun removeFile(media: Media): String {
        return "1"
    }
}