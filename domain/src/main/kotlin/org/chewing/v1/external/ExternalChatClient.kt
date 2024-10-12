package org.chewing.v1.external

import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.model.chat.log.ChatLog1


interface ExternalChatClient {
    fun sendMessage(chatMessage: ChatLog1)
}