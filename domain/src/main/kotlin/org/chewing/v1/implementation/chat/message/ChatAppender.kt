package org.chewing.v1.implementation.chat.message


import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.ChatLogRepository
import org.springframework.stereotype.Component

@Component
class ChatAppender(
    private val chatLogRepository: ChatLogRepository
) {
    fun appendChatLog(chatMessage: ChatMessage){
        chatLogRepository.appendChatLog(chatMessage)
    }
}

