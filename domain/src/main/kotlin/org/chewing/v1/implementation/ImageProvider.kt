package org.chewing.v1.implementation

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.external.ExternalImageClient
import org.springframework.stereotype.Component
import java.io.File

/**
 * ImageProvider 이미지 관련 데이터를 저장 및 삭제하는 구현체입니다.
 * 이미지 삭제 추가 시, 실패하는 경우 적절한 예외를 발생시킵니다.
 */
@Component
class ImageProvider(private val externalImageClient: ExternalImageClient) {

    /**
     * 주어진 이미지 파일을 추가합니다.
     * @throws ConflictException 이미지 파일 추가에 실패하는 경우,
     * IMAGE_UPLOAD_FAILED 오류 코드와 함께 예외를 발생시킵니다.
     */
    fun appendImage(file: File, dirName: String) {
        try {
            externalImageClient.uploadImage(file, dirName)
        } catch (e: Exception) {
            throw ConflictException(ErrorCode.IMAGE_UPLOAD_FAILED)
        }
    }

    /**
     * 주어진 이미지 파일을 삭제합니다.
     * @throws ConflictException 이미지 파일 삭제에 실패하는 경우,
     * IMAGE_DELETE_FAILED 오류 코드와 함께 예외를 발생시킵니다.
     */
    fun removeImage(fileUrl: String) {
        try {
            externalImageClient.removeImage(fileUrl)
        } catch (e: Exception) {
            throw ConflictException(ErrorCode.IMAGE_DELETE_FAILED)
        }
    }
}