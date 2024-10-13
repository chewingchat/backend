package org.chewing.v1.implementation.chat.message


import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.repository.ChatLogRepository
import org.springframework.stereotype.Component

@Component
class ChatReader(
    private val chatLogRepository: ChatLogRepository
) {
    fun readChatLog(chatRoomId: String, page: Int): List<ChatMessage> {
        return chatLogRepository.readChatMessages(chatRoomId, page)
    }

    fun readChatMessage(messageId: String): ChatMessage {
        return chatLogRepository.readChatMessage(messageId) ?: throw NotFoundException(ErrorCode.CHATLOG_NOT_FOUND)
    }
}