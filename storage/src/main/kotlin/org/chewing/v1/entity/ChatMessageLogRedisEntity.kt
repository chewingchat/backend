package org.chewing.v1.entity

import jakarta.persistence.Id
import org.chewing.v1.model.ChatMessage
import org.chewing.v1.model.ChatMessage.FriendSeqInfo
import org.chewing.v1.model.ChatMessageLog
import org.chewing.v1.model.MessageType
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime


@RedisHash
data class ChatMessageLogRedisEntity(
    @Id val id: String,
    val roomId: String,
    val page: Int,
    val chatMessages: List<ChatMessageRedisEntity>,
    val friends: List<FriendSeqInfo>? = null
) {
    fun toChatMessageLog(): ChatMessageLog {
        return ChatMessageLog(
            page = this.page,
            roomId = this.roomId,
            messageList = this.chatMessages.map { it.toChatMessage() }, // roomId 전달
            friends = this.friends
        )
    }

    companion object {
        fun toEntity(chatMessageLog: ChatMessageLog): ChatMessageLogRedisEntity {
            return ChatMessageLogRedisEntity(
                "${chatMessageLog.roomId}:${chatMessageLog.page}",
                chatMessageLog.roomId,
                chatMessageLog.page,
                chatMessageLog.messageList.map { ChatMessageRedisEntity.toEntity(it) }
            )
        }
    }


    data class ChatMessageRedisEntity(
        val messageId: String,
        val type: MessageType,
        val sender: String,
        val message: String,
        val timestamp: LocalDateTime,

        val messageType: String?,  // 파일 메시지 타입 추가
        val parentMessageId: String?,  // 대댓글 기능을 위한 부모 메시지 ID
        val parentMessagePage: Int?,   // 부모 메시지 페이지 번호
        val parentMessageSeqNumber: Int?, // 부모 메시지 시퀀스 번호
    ) {
        fun toChatMessage(): ChatMessage {
            return ChatMessage(
                chatId = ChatMessage.ChatId(messageId),
                type = this.type,
                roomId = null,
                sender = this.sender,
                text = this.message,
                timestamp = this.timestamp,
                messageType = this.messageType,
                parentMessageId = this.parentMessageId,
                parentMessagePage = this.parentMessagePage,
                parentMessageSeqNumber = this.parentMessageSeqNumber,
                messageId = this.messageId


            )
        }


        companion object {
            fun toEntity(chatMessage: ChatMessage): ChatMessageRedisEntity {
                return ChatMessageRedisEntity(
                    chatMessage.chatId!!.id,
                    chatMessage.type,
                    chatMessage.sender,
                    chatMessage.text!!,
                    chatMessage.timestamp!!,
                    chatMessage.messageType,
                    chatMessage.parentMessageId,
                    chatMessage.parentMessagePage,
                    chatMessage.parentMessageSeqNumber,
                )
            }
        }
    }
}