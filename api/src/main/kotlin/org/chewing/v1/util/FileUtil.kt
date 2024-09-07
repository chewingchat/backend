package org.chewing.v1.util

import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ErrorCode
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object FileUtil {
    @Throws(IOException::class, AuthorizationException::class)
    fun convertMultipartFileToFile(file: MultipartFile): File {
        val originalFilename = file.originalFilename ?: throw AuthorizationException(ErrorCode.FILE_NAME_COULD_NOT_EMPTY)
        val convertFile = File(originalFilename)
        val parentDir = convertFile.parentFile
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs()
        }
        if (convertFile.exists()) {
            throw AuthorizationException(ErrorCode.FILE_CONVERT_FAILED)
        }
        if (!convertFile.createNewFile()) {
            throw AuthorizationException(ErrorCode.FILE_CONVERT_FAILED)
        }

        FileOutputStream(convertFile).use { fos ->
            fos.write(file.bytes)
        }

        return convertFile
    }
}