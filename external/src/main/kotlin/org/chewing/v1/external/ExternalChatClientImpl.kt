package org.chewing.v1.external

import org.chewing.v1.model.chat.message.ChatMessage
import org.springframework.stereotype.Component

@Component
class ExternalChatClientImpl : ExternalChatClient {
    override fun sendMessage(chatMessage: ChatMessage, sessionIds: List<String>) {
        println()
    }

    override fun sendMessages(chatMessages: List<ChatMessage>) {

    }
}