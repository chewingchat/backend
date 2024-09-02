package org.chewing.v1.error

import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

enum class ErrorCode(
    val code: String,
    val message: String
) {
    // Common
    INTERNAL_SERVER_ERROR("COMMON_001", "Internal Server Error"),
    FILE_CONVERT_FAILED("FILE_001", "파일 변환 실패"),
    FILE_NAME_COULD_NOT_EMPTY("FILE_002", "파일 이름 없음"),
    USER_NOT_FOUND("USER_001", "회원을 찾을 수 없음."),
    USER_CREATE_FAILED("USER_002", "사용자 저장 실패 실패."),
    USER_UPDATE_FAILED("USER_003", "사용자 정보 수정 실패."),
    FRIEND_NOT_FOUND("FRIEND_001", "친구를 찾을 수 없음."),
    FRIEND_ALREADY_CREATED("FRIEND_002", "이미 추가된 친구입니다."),
    IMAGE_UPLOAD_FAILED("IMAGE_001", "이미지 업로드 실패."),
    IMAGE_DELETE_FAILED("IMAGE_002", "이미지 삭제 실패"),
    FEED_NOT_FOUND("FEED_001", "피드를 찾을 수 없음."),
    ;

    companion object {
        private val ERROR_CODE_MAP: Map<String, ErrorCode> = Stream.of(*entries.toTypedArray())
            .collect(Collectors.toMap(ErrorCode::message, Function.identity()))

        fun from(message: String?): ErrorCode? {
            if (ErrorCode.ERROR_CODE_MAP.containsKey(message)) {
                return ErrorCode.ERROR_CODE_MAP.get(message)
            }

            return ErrorCode.INTERNAL_SERVER_ERROR
        }
    }
}
