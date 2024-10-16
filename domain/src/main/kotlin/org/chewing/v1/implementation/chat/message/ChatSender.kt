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
    private val webSocketProvider: WebSocketProvider
) {

    fun sendChat(chatLog: ChatMessage, chatRoomMemberInfos: List<ChatRoomMemberInfo>) {
        val userIds = chatRoomMemberInfos.map { it.memberId }
        userIds.forEach {
            val sessionIds = webSocketProvider.readAll(it)
            if(sessionIds.isNotEmpty()) {
                sessionIds.forEach { sessionId ->
                    externalChatClient.sendMessage(chatLog, sessionIds)
                }
            }
            else{
                // notification 호출
            }
        }
    }

    fun sendsChat(chatLogs: List<ChatMessage>) {
        externalChatClient.sendMessages(chatLogs)
    }
}
