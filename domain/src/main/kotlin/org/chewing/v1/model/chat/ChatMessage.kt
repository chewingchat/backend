package org.chewing.v1.model.chat

import org.chewing.v1.model.media.MediaType
import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage(
    val messageId: String,  // Use messageId instead of chatId
    val type: MessageType,
    val roomId: String?,
    val sender: String,
    val messageSendType: MessageSendType?,
    val parentMessageId: String?,
    val parentMessagePage: Int?,
    val parentSeqNumber: Int?,
    val timestamp: LocalDateTime?,
    var friends: List<FriendSeqInfo>? = null,
    val seqNumber: Int? = null,
    val page: Int
) {
    companion object {
        fun withId(
            messageId: String,  // Use messageId
            type: MessageType,
            sender: String,
            messageSendType: MessageSendType,
            parentMessageId: String?,
            parentMessagePage: Int?,
            parentMessageSeqNumber: Int?,
            timestamp: LocalDateTime,
            friends: List<FriendSeqInfo>?,
            page: Int
        ): ChatMessage {
            return ChatMessage(
                messageId = messageId,  // messageId is used
                type = type,
                roomId = null,
                sender = sender,
                messageSendType = messageSendType,
                parentMessageId = parentMessageId,
                parentMessagePage = parentMessagePage,
                parentSeqNumber = parentMessageSeqNumber,
                timestamp = timestamp,
                friends = friends,
                seqNumber = null,
                page = page
            )
        }

        fun withoutId(
            type: MessageType,
            roomId: String,
            sender: String,
            messageSendType: MessageSendType,
            parentMessageId: String?,
            parentMessagePage: Int?,
            parentMessageSeqNumber: Int?,
            friends: List<FriendSeqInfo>?,
            page: Int
        ): ChatMessage {
            return ChatMessage(
                messageId = UUID.randomUUID().toString(),  // Generate a unique messageId if not provided
                type = type,
                roomId = roomId,
                sender = sender,
                messageSendType = messageSendType,
                parentMessageId = parentMessageId,
                parentMessagePage = parentMessagePage,
                parentSeqNumber = parentMessageSeqNumber,
                timestamp = null,
                friends = friends,
                seqNumber = null,
                page = page
            )
        }
    }
    data class FriendSeqInfo(val friendId: Int, val friendSeqNumber: Int)
}



