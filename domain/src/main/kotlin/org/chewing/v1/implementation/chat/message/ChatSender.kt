package org.chewing.v1.implementation.chat.message

import org.chewing.v1.external.ExternalChatClient
import org.chewing.v1.model.chat.ChatRoomMemberInfo
import org.chewing.v1.model.chat.message.ChatMessage
import org.springframework.stereotype.Component

@Component
class ChatSender(private val externalChatClient: ExternalChatClient) {

    fun sendChat(chatLog: ChatMessage, chatRoomMemberInfos : List<ChatRoomMemberInfo>) {
        externalChatClient.sendMessage(chatLog)
    }

    fun sendsChat(chatLogs: List<ChatMessage>) {
        externalChatClient.sendMessages(chatLogs)
    }
}
