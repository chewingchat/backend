package org.chewing.v1.model.chat



data class MessageResponse(
    val type: String,
    val messageId: String,
    val senderId: String,
    val message: String,
    val messageSendTime: String,
    val messageSeqNumber: Int,
    val parentMessageId: String? = null,
    val parentMessage: String? = null,
    val parentMessagePage: Int? = null,
    val parentMessageSeqNumber: Int?  // 부모 메시지 시퀀스 번호 (nullable)
)