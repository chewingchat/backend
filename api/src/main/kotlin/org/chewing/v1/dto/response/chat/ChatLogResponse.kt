package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.log.ChatLog
import org.chewing.v1.model.chat.message.ChatMessage

data class ChatLogResponse(
    val chatRoomId: String,
    val currentPage: Int,
    val chatMessages: List<ChatLogMessageResponse>
) {
    companion object {
        fun from(list: List<ChatLog>): ChatLogResponse {
            return ChatLogResponse(
                chatRoomId = list.first().chatRoomId,
                currentPage = list.first().number.page,
                chatMessages = list.map { ChatLogMessageResponse.from(it) }
            )
        }
    }
}