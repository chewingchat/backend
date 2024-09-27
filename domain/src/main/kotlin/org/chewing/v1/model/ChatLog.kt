package org.chewing.v1.model

data class ChatLog(
    val type: String,
    val messageId: String,
    val senderId: String,
    val message: String,
    val messageSendTime: String,
    val messageSeqNumber: Int,  // 시퀀스 번호 추가
    val parentMessageId: String? = null,  // 부모 메시지 ID 추가
    val parentMessage: String? = null,  // 부모 메시지 내용 추가
    val parentMessagePage: Int? = null,  // 부모 메시지 페이지 번호 추가
    val parentMessageSeqNumber: Int? = null,  // 부모 메시지 시퀀스 번호 추가
    val page: Int? = null
)
