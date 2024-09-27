package org.chewing.v1.implementation.chat


import org.chewing.v1.model.ChatMessage
import org.chewing.v1.repository.ChatMessageCacheRepository
import org.chewing.v1.repository.ChatMessageRepository
import org.springframework.stereotype.Component

@Component
class ChatLogAppender(private val chatMessageRepository: ChatMessageRepository) {

    fun appendChatMessage(chatMessage: ChatMessage, page: Int) {
        chatMessageRepository.appendChatMessage(chatMessage, page)
    }
    // 메시지 삭제
    fun deleteMessage(roomId: String, messageId: String) {
        // 레포지토리 레이어에 메시지 삭제 요청
        chatMessageRepository.deleteMessage(roomId, messageId)
    }
}

