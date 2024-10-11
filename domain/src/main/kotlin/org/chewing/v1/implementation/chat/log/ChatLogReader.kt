package org.chewing.v1.implementation.chat.log


import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.repository.ChatMessageRepository
import org.springframework.stereotype.Component

@Component
class ChatLogReader(private val chatMessageRepository: ChatMessageRepository) {

    fun readChatLog(roomId: String, page: Int): List<ChatMessage> {
        return chatMessageRepository.readChatMessages(roomId, page)
    }
}