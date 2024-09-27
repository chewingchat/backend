package org.chewing.v1.external

import org.chewing.v1.model.ChatMessage

interface ExternalChatClient {
    fun sendMessage(chatMessage: ChatMessage)
}