package org.chewing.v1.implementation.media

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.external.ExternalImageClient
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import java.io.File

/**
 * ImageProvider 이미지 관련 데이터를 저장 및 삭제하는 구현체입니다.
 * 이미지 삭제 추가 시, 실패하는 경우 적절한 예외를 발생시킵니다.
 */
@Component
class FileProvider(private val externalImageClient: ExternalImageClient) {

    /**
     * 주어진 이미지 파일을 추가합니다.
     * @throws ConflictException 이미지 파일 추가에 실패하는 경우,
     * IMAGE_UPLOAD_FAILED 오류 코드와 함께 예외를 발생시킵니다.
     */
    fun appendFile(file: FileData, media: Media) {
        try {
            externalImageClient.uploadFile(file, media)
        } catch (e: Exception) {
            throw ConflictException(ErrorCode.FILE_UPLOAD_FAILED)
        }
    }

    /**
     * 주어진 이미지 파일을 삭제합니다.
     * @throws ConflictException 이미지 파일 삭제에 실패하는 경우,
     * IMAGE_DELETE_FAILED 오류 코드와 함께 예외를 발생시킵니다.
     */
    fun removeFile(media: Media) {
        try {
            externalImageClient.removeFile(media)
        } catch (e: Exception) {
            throw ConflictException(ErrorCode.FILE_DELETE_FAILED)
        }
    }
}