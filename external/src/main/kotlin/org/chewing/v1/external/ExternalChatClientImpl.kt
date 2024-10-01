package org.chewing.v1.external

import org.chewing.v1.model.chat.ChatMessage
import org.springframework.stereotype.Component

@Component
class ExternalChatClientImpl: ExternalChatClient  {
    override fun sendMessage(chatMessage: ChatMessage) {
        println()
    }
}