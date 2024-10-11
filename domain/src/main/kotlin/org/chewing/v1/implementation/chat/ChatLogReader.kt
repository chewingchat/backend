package org.chewing.v1.implementation.chat


import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.repository.ChatMessageRepository
import org.springframework.stereotype.Component

@Component
class ChatLogReader(private val chatMessageRepository: ChatMessageRepository) {

    fun readChatLog(roomId: String, page: Int): ChatLog {
        return chatMessageRepository.readChatMessages(roomId, page)
    }
}