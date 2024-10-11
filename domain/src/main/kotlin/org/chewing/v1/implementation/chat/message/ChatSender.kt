package org.chewing.v1.implementation.chat.message

import org.chewing.v1.external.ExternalChatClient
import org.chewing.v1.model.chat.ChatMessage
import org.springframework.stereotype.Component

@Component
class ChatSender(private val externalChatClient: ExternalChatClient) {

    fun sendChat(chatMessage: ChatMessage) {
        externalChatClient.sendMessage(chatMessage)
    }
}
