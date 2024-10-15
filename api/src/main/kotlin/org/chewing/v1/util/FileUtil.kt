package org.chewing.v1.util

import com.nimbusds.jose.util.IOUtils
import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.MediaType
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.socket.BinaryMessage
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths


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

    @Throws(IOException::class, AuthorizationException::class)
    fun convertBinaryMessageToFileData(message: BinaryMessage, fileName: String, contentType: String): FileData {
        val mediaType = MediaType.fromType(contentType)
            ?: throw AuthorizationException(ErrorCode.NOT_SUPPORT_FILE_TYPE)

        // BinaryMessage의 payload에서 InputStream 생성
        val inputStream = ByteArrayInputStream(message.payload.array())

        return FileData.of(
            inputStream,
            mediaType,
            fileName,
            message.payloadLength.toLong()
        )
    }


}