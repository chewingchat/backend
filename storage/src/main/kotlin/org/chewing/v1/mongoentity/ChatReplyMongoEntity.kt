package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.message.ChatReplyMessage
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
internal class ChatReplyMongoEntity(
    messageId: String,
    chatRoomId: String,
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
    messageId = messageId,
    chatRoomId = chatRoomId,
    senderId = senderId,
    seqNumber = seqNumber,
    page = page,
    sendTime = sendTime,
    type = MessageType.REPLY,
) {
    override fun toChatMessage(): ChatMessage {
        return ChatReplyMessage.of(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = senderId,
            parentMessageId = parentMessageId,
            parentMessagePage = parentMessagePage,
            parentSeqNumber = parentSeqNumber,
            parentMessageText = parentMessageText,
            text = message,
            timestamp = sendTime,
            number = ChatNumber.of(chatRoomId, seqNumber, page),
            )
    }
}