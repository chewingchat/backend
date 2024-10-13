package org.chewing.v1.external

import org.chewing.v1.model.chat.message.ChatMessage


interface ExternalChatClient {
    fun sendMessage(chatMessage: ChatMessage)
    fun sendMessages(chatMessages: List<ChatMessage>)
}