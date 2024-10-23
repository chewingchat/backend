package org.chewing.v1.implementation.chat.message

import org.chewing.v1.external.ExternalChatClient
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.stereotype.Component

@Component
class ChatSender(
    private val externalChatClient: ExternalChatClient,
    private val asyncJobExecutor: AsyncJobExecutor
) {

    fun sendsChat(chatLog: ChatMessage, userIds: List<String>) {
        asyncJobExecutor.executeAsyncJobs(userIds) {
            externalChatClient.sendMessage(chatLog, it)
        }
    }

    fun sendChat(chatLog: ChatMessage, userId: String) {
        externalChatClient.sendMessage(chatLog, userId)
    }
}
