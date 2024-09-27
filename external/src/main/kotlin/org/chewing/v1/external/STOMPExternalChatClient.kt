package org.chewing.v1.external

import org.chewing.v1.model.chat.ChatMessage
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class STOMPExternalChatClient: ExternalChatClient  {
    override fun sendMessage(chatMessage: ChatMessage) {
        println()
    }
}