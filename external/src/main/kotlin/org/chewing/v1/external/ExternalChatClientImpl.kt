package org.chewing.v1.external

import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.model.chat.log.ChatLog1
import org.springframework.stereotype.Component

@Component
class ExternalChatClientImpl : ExternalChatClient {
    override fun sendMessage(chatMessage: ChatLog1) {
        println()
    }
}