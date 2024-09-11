package org.chewing.v1.error

import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

enum class ErrorCode(
    val code: String,
    val message: String
) {
    // Common
    PATH_WRONG("COMMON_001", "잘못된 메세드입니다."),
    VARIABLE_WRONG("COMMON_002", "요청 변수가 잘못되었습니다."),
    INTERNAL_SERVER_ERROR("COMMON_003", "Internal Server Error"),
    FILE_CONVERT_FAILED("FILE_001", "파일 변환 실패"),
    FILE_NAME_COULD_NOT_EMPTY("FILE_002", "파일 이름 없음"),
    USER_NOT_FOUND("USER_001", "회원을 찾을 수 없음."),
    USER_CREATE_FAILED("USER_002", "사용자 저장 실패 실패."),
    USER_UPDATE_FAILED("USER_003", "사용자 정보 수정 실패."),
    FRIEND_NOT_FOUND("FRIEND_001", "친구를 찾을 수 없음."),
    FRIEND_ALREADY_CREATED("FRIEND_002", "이미 추가된 친구입니다."),
    FILE_UPLOAD_FAILED("IMAGE_001", "파일 업로드 실패."),
    FILE_DELETE_FAILED("IMAGE_002", "파일 삭제 실패"),
    FEED_NOT_FOUND("FEED_001", "피드를 찾을 수 없음."),
    FEED_ALREADY_LIKED("FEED_002", "이미 공감한 피드입니다."),
    FEED_ALREADY_UNLIKED("FEED_003", "이미 공감을 취소한 피드입니다."),
    FEED_IS_NOT_OWNED("FEED_004", "피드 작성자가 아닙니다."),
    FEED_IS_OWNED("FEED_005", "피드 작성자입니다."),
    COMMENT_IS_NOT_OWNED("COMMENT_001", "댓글 작성자가 아닙니다."),
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
