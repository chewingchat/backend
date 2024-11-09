package org.chewing.v1.external

import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component

@Component
class ExternalFileClientImpl : ExternalFileClient {

    override fun uploadFile(file: FileData, media: Media) {
    }

    override fun removeFile(media: Media) {
    }
}
