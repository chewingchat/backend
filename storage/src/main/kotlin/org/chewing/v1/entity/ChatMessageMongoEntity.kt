package org.chewing.v1.entity

import org.chewing.v1.model.ChatMessage
import org.chewing.v1.model.MessageType
import java.time.LocalDateTime
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ChatMessageMongoEntity(
    val id: String,
    val page: Int,
    val type: MessageType,
    val roomId: String,
    val sender: String,
    val message: String,
    val timestamp: LocalDateTime,
    val friends: List<ChatMessage.FriendSeqInfo>? = null, // friends 필드 추가

    val messageType: String?,  // 파일 메시지 타입 추가
    val messageId: String?,    // 메시지 삭제 및 식별을 위한 messageId 추가
    val parentMessageId: String?,  // 대댓글 기능을 위한 부모 메시지 ID
    val parentMessagePage: Int?,   // 부모 메시지 페이지 번호
    val parentMessageSeqNumber: Int?, // 부모 메시지 시퀀스 번호
) {
    fun toChatMessage(): ChatMessage {
        return ChatMessage(
            chatId = ChatMessage.ChatId(id),
            type = this.type,
            roomId = this.roomId,
            sender = this.sender,
            text = this.message,
            timestamp = this.timestamp,
            friends = this.friends, // friends 필드 처리 추가
            messageId = this.messageId,
            messageType = this.messageType,
            parentMessageId = this.parentMessageId,
            parentMessagePage = this.parentMessagePage,
            parentMessageSeqNumber = this.parentMessageSeqNumber

        )
    }

    companion object {
        fun toEntity(chatMessage: ChatMessage, page: Int): ChatMessageMongoEntity {
            return ChatMessageMongoEntity(
                chatMessage.chatId!!.id,
                page,
                chatMessage.type,
                chatMessage.roomId!!,
                chatMessage.sender,
                chatMessage.text!!,
                chatMessage.timestamp!!,
                chatMessage.friends, // friends 필드 추가
                chatMessage.messageId,
                chatMessage.messageType,
                chatMessage.parentMessageId,
                chatMessage.parentMessagePage,
                chatMessage.parentMessageSeqNumber
            )
        }
    }
}
