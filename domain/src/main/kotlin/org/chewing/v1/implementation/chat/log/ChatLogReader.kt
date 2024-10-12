package org.chewing.v1.implementation.chat.log


import org.chewing.v1.model.chat.log.ChatLog1
import org.chewing.v1.repository.ChatLogRepository
import org.springframework.stereotype.Component

@Component
class ChatLogReader(
    private val chatLogRepository: ChatLogRepository
) {

    fun readChatLog(roomId: String, page: Int): List<ChatLog1> {
        return chatLogRepository.readChatMessages(roomId, page)
    }
}