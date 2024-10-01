package org.chewing.v1.util

import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.MediaType
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


object FileUtil {
    @Throws(IOException::class, AuthorizationException::class)
    fun convertMultipartFileToFileData(file: MultipartFile): FileData {
        val originalFilename =
            file.originalFilename ?: throw AuthorizationException(ErrorCode.FILE_NAME_COULD_NOT_EMPTY)
        val mediaType =
            MediaType.fromType(file.contentType ?: throw AuthorizationException(ErrorCode.NOT_SUPPORT_FILE_TYPE))
                ?: throw AuthorizationException(ErrorCode.NOT_SUPPORT_FILE_TYPE)

        return FileData.of(
            file.inputStream,
            mediaType,
            originalFilename,
            file.size
        )
    }

    @Throws(IOException::class, AuthorizationException::class)
    fun convertMultipartFileToFileDataList(files: List<MultipartFile>): List<FileData> {
        return files.map { convertMultipartFileToFileData(it) }
    }
}