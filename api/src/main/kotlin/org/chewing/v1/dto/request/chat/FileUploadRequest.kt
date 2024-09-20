package org.chewing.v1.dto.request.chat

data class FileUploadRequest(
    val chatRoomId: String,
    val files: List<String> // 파일의 경로나 파일명으로 처리
)