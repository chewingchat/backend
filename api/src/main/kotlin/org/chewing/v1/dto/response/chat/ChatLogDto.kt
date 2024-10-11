package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatMessage

data class ChatLogDto(
    val roomId: String?,
    val currentPage: Int?,
    val chatMessages: List<ChatMessage>?,
    val friends: List<ChatMessage.FriendSeqInfo>? // friends 필드 추가
) {
    companion object {
        fun from(chatLog: ChatLog): ChatLogDto {
            return ChatLogDto(
                roomId = chatLog.roomId,
                currentPage = chatLog.page,
                chatMessages = chatLog.chatMessages,
                friends = chatLog.friends // friends 추가
            )
        }
    }
}