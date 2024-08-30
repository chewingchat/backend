package org.chewing.v1.external

import org.springframework.stereotype.Repository
import java.io.File

@Repository
interface ExternalImageClient {
    fun uploadImage(file: File, dirName: String): String
    fun removeImage(fileUrl: String): String
}