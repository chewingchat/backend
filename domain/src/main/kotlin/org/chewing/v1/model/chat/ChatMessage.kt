package org.chewing.v1.model.chat

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage(
    val chatId: ChatId?,
    val type: MessageType,
    val roomId: String?,
    val sender: String,
    val text: String?,
    val messageType: String?,  // 파일 메시지 타입 추가
    val messageId: String?,    // 메시지 삭제 및 식별을 위한 messageId 추가
    val parentMessageId: String?,  // 대댓글 기능을 위한 부모 메시지 ID
    val parentMessagePage: Int?,   // 부모 메시지 페이지 번호
    val parentMessageSeqNumber: Int?, // 부모 메시지 시퀀스 번호
    val timestamp: LocalDateTime?,
    var friends: List<FriendSeqInfo>?= null // 친구 목록 및 읽음 상태 추가
) {
    companion object {
        // chatId가 있는 상태에서 ChatMessage 생성 (기존 메시지 수정, 조회용)
        fun withId(
            id: String,
            type: MessageType,
            sender: String,
            message: String,
            messageType: String?,
            messageId: String?,
            parentMessageId: String?,
            parentMessagePage: Int?,
            parentMessageSeqNumber: Int?,
            timestamp: LocalDateTime
        ): ChatMessage {
            return ChatMessage(
                ChatId.of(id),
                type,
                null,
                sender,
                message,
                messageType,
                messageId,
                parentMessageId,
                parentMessagePage,
                parentMessageSeqNumber,
                timestamp
            )
        }

        // chatId 없이 새로운 ChatMessage 생성
        fun withoutId(
            type: MessageType,
            roomId: String,
            sender: String,
            message: String?,
            messageType: String?,
            messageId: String?,
            parentMessageId: String?,
            parentMessagePage: Int?,
            parentMessageSeqNumber: Int?,
            friends: List<FriendSeqInfo>? = null
        ): ChatMessage {
            return ChatMessage(
                null,
                type,
                roomId,
                sender,
                message,
                messageType,
                messageId,
                parentMessageId,
                parentMessagePage,
                parentMessageSeqNumber,
                null
            )
        }
    }

    data class ChatId(val id: String) {
        companion object {
            fun generate(): ChatId {
                return ChatId(UUID.randomUUID().toString())
            }

            fun of(id: String): ChatId {
                return ChatId(id)
            }
        }
    }

    data class FriendSeqInfo(val friendId: String, val friendSeqNumber: Int) // 친구 시퀀스 정보 데이터 클래스 추가
}

