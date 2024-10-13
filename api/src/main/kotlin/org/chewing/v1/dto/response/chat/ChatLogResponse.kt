package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.message.ChatMessage

data class ChatLogResponse(
    val chatRoomId: String,
    val currentPage: Int,
    val chatMessages: List<ChatLogMessageResponse>
) {
    companion object {
        fun from(list: List<ChatMessage>): ChatLogResponse {
            return ChatLogResponse(
                chatRoomId = list.first().chatRoomId,
                currentPage = list.first().page,
                chatMessages = list.map { ChatLogMessageResponse.from(it) }
            )
        }
    }
}