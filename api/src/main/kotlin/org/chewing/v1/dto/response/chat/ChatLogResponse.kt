package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.log.ChatLog1

data class ChatLogResponse(
    val roomId: String,
    val currentPage: Int,
    val chatMessages: List<ChatLogMessageResponse>
) {
    companion object {
        fun from(list: List<ChatLog1>): ChatLogResponse {
            return ChatLogResponse(
                roomId = list.first().roomId,
                currentPage = list.first().page,
                chatMessages = list.map { ChatLogMessageResponse.from(it) }
            )
        }
    }
}