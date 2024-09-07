package org.chewing.v1.aws

import org.chewing.v1.external.ExternalImageClient
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class AWSExternalImageClient : ExternalImageClient {

    override fun uploadImage(file: File, dirName: String): String {
        return "1"
    }

    override fun removeImage(fileUrl: String): String {
        return "1"
    }
}