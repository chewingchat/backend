package org.chewing.v1.external

import org.chewing.v1.model.chat.ChatMessage


interface ExternalChatClient {
    fun sendMessage(chatMessage: ChatMessage)
}