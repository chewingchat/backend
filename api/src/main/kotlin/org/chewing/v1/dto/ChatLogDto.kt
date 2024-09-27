package org.chewing.v1.dto

import org.chewing.v1.model.ChatMessage
import org.chewing.v1.model.ChatMessageLog

data class ChatLogDto(
    val roomId: String,
    val currentPage: Int,
    val chatMessages: List<ChatDto>,
    val friends: List<ChatMessage.FriendSeqInfo>? // friends 필드 추가
) {
    companion object {
        fun from(chatMessageLog: ChatMessageLog): ChatLogDto {
            return ChatLogDto(
                roomId = chatMessageLog.roomId,
                currentPage = chatMessageLog.page,
                chatMessages = chatMessageLog.messageList.map { ChatDto.from(it) },
                friends = chatMessageLog.friends // friends 추가
            )
        }
    }
}