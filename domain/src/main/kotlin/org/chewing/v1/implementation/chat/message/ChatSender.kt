package org.chewing.v1.implementation.chat.message

import org.chewing.v1.external.ExternalChatClient
import org.chewing.v1.external.ExternalNotificationClient
import org.chewing.v1.implementation.WebSocketProvider
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.message.ChatMessage
import org.springframework.stereotype.Component

@Component
class ChatSender(
    private val externalChatClient: ExternalChatClient,
) {

    fun sendChat(chatLog: ChatMessage, activeUserIds: List<String>) {
        externalChatClient.sendMessage(chatLog, activeUserIds)
    }

    fun sendsChat(chatLogs: List<ChatMessage>) {
        externalChatClient.sendMessages(chatLogs)
    }
}
