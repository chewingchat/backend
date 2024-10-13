package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.message.ChatLeaveMessage
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
internal class ChatLeaveMongoEntity(
    messageId: String,
    chatRoomId: String,
    senderId: String,
    seqNumber: Int,
    page: Int,
    sendTime: LocalDateTime,
) : ChatMessageMongoEntity(
    messageId = messageId,
    chatRoomId = chatRoomId,
    senderId = senderId,
    type = MessageType.LEAVE,
    seqNumber = seqNumber,
    page = page,
    sendTime = sendTime
) {

    companion object {
        fun from(
            chatLeaveMessage: ChatLeaveMessage
        ): ChatLeaveMongoEntity {
            return ChatLeaveMongoEntity(
                messageId = chatLeaveMessage.messageId,
                chatRoomId = chatLeaveMessage.chatRoomId,
                senderId = chatLeaveMessage.senderId,
                seqNumber = chatLeaveMessage.number.sequenceNumber,
                page = chatLeaveMessage.number.page,
                sendTime = chatLeaveMessage.timestamp,
            )
        }
    }

    override fun toChatMessage(): ChatMessage {
        return ChatLeaveMessage.of(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = senderId,
            timestamp = sendTime,
            number = ChatNumber.of(chatRoomId, seqNumber, page),

            )
    }
}