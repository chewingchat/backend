package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.log.ChatLog1
import org.chewing.v1.model.chat.log.ChatReplyLog
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
internal class ChatReplyMongoEntity(
    roomId: String,
    senderId: String,
    seqNumber: Int,
    page: Int,
    sendTime: LocalDateTime,
    private val message: String,
    private val parentMessageId: String,
    private val parentMessagePage: Int,
    private val parentSeqNumber: Int,
    private val parentMessageText: String,
) : ChatMessageMongoEntity(
    roomId = roomId,
    senderId = senderId,
    seqNumber = seqNumber,
    page = page,
    sendTime = sendTime,
    type = MessageType.REPLY,
) {
    override fun toChatMessage(): ChatLog1 {
        return ChatReplyLog.of(
            messageId = messageId,
            roomId = roomId,
            senderId = senderId,
            parentMessageId = parentMessageId,
            parentMessagePage = parentMessagePage,
            parentSeqNumber = parentSeqNumber,
            parentMessageText = parentMessageText,
            text = message,
            seqNumber = seqNumber,
            timestamp = sendTime,
            page = page
        )
    }
}