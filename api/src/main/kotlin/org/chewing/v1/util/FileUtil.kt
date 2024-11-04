package org.chewing.v1.util

import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.MediaType
import org.springframework.web.multipart.MultipartFile
import java.io.*


object FileUtil {
    @Throws(IOException::class, ConflictException::class)
    fun convertMultipartFileToFileData(file: MultipartFile): FileData {
        val originalFilename =
            file.originalFilename ?: throw ConflictException(ErrorCode.FILE_NAME_COULD_NOT_EMPTY)
        val mediaType =
            MediaType.fromType(file.contentType ?: throw ConflictException(ErrorCode.NOT_SUPPORT_FILE_TYPE))
                ?: throw ConflictException(ErrorCode.NOT_SUPPORT_FILE_TYPE)

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