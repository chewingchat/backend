package org.chewing.v1.dto

import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.model.chat.MessageType

import java.time.LocalDateTime

data class ChatDto(
    val type: MessageType,
    val roomId: String?,
    val sender: String,
    val message: String?,
    val messageType: String?,  // 파일 메시지 타입 추가
    val timestamp: LocalDateTime?,
    val messageId: String?,
    val parentMessageId: String?,  // 부모 메시지 ID 추가
    val parentMessagePage: Int?,   // 부모 메시지 페이지
    val parentMessageSeqNumber: Int?, // 부모 메시지 시퀀스 번호
    val friends: List<ChatMessage.FriendSeqInfo>? = null // 친구 목록 및 읽음 상태 추가
) {
    companion object {
        fun from(chatMessage: ChatMessage) = ChatDto(
            chatMessage.type,
            chatMessage.roomId,
            chatMessage.sender,
            chatMessage.text,
            chatMessage.messageType,
            chatMessage.timestamp,
            chatMessage.messageId,
            chatMessage.parentMessageId,
            chatMessage.parentMessagePage,
            chatMessage.parentMessageSeqNumber,
            chatMessage.friends
        )
    }

    fun toChat() = ChatMessage.withoutId(
        type,
        roomId!!,
        sender,
        message,
        messageType,
        messageId,
        parentMessageId,
        parentMessagePage,
        parentMessageSeqNumber,
        friends
    )
}