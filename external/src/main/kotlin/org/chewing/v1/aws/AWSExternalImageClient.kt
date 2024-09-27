package org.chewing.v1.aws

import org.chewing.v1.external.ExternalImageClient
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class AWSExternalImageClient : ExternalImageClient {

    override fun uploadFile(file: File, media: Media): String {
        return "1"
    }

    override fun removeFile(media: Media): String {
        return "1"
    }
}