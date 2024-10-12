package org.chewing.v1.implementation.chat.message

import org.chewing.v1.external.ExternalChatClient
import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.model.chat.log.ChatLog1
import org.springframework.stereotype.Component

@Component
class ChatSender(private val externalChatClient: ExternalChatClient) {

    fun sendChat(chatMessage: ChatLog1) {
        externalChatClient.sendMessage(chatMessage)
    }
}
