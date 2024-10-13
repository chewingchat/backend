package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.message.ChatCommonMessage
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
internal class ChatCommonMongoEntity(
    messageId: String,
    chatRoomId: String,
    senderId: String,
    seqNumber: Int,
    page: Int,
    sendTime: LocalDateTime,
    private val message: String
) : ChatMessageMongoEntity(
    messageId = messageId,
    chatRoomId = chatRoomId,
    senderId = senderId,
    type = MessageType.CHAT,
    seqNumber = seqNumber,
    page = page,
    sendTime = sendTime
) {
    companion object {
        fun from(
            chatCommonMessage: ChatCommonMessage
        ): ChatCommonMongoEntity {
            return ChatCommonMongoEntity(
                messageId = chatCommonMessage.messageId,
                chatRoomId = chatCommonMessage.chatRoomId,
                senderId = chatCommonMessage.senderId,
                seqNumber = chatCommonMessage.number.sequenceNumber,
                page = chatCommonMessage.number.page,
                sendTime = chatCommonMessage.timestamp,
                message = chatCommonMessage.text
            )
        }
    }

    override fun toChatMessage(): ChatMessage {
        return ChatCommonMessage.of(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = senderId,
            timestamp = sendTime,
            number = ChatNumber.of(chatRoomId, seqNumber, page),
            text = message
        )
    }
}