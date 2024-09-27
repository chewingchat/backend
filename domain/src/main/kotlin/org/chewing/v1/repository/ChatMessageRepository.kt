package org.chewing.v1.repository

import org.chewing.v1.model.ChatMessage
import org.chewing.v1.model.ChatMessageLog

interface ChatMessageRepository {
    fun appendChatMessage(chatMessage: ChatMessage, page: Int)
    fun readChatMessages(roomId: String, page: Int): ChatMessageLog
    fun deleteMessage(roomId: String, messageId: String)  // 메시지 삭제 기능 추가
}