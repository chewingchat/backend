package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.log.ChatCommonLog
import org.chewing.v1.model.chat.log.ChatLog1
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
internal class ChatCommonMongoEntity(
    roomId: String,
    senderId: String,
    seqNumber: Int,
    page: Int,
    sendTime: LocalDateTime,
    private val message: String
) : ChatMessageMongoEntity(
    roomId = roomId,
    senderId = senderId,
    type = MessageType.CHAT,
    seqNumber = seqNumber,
    page = page,
    sendTime = sendTime
) {
    override fun toChatMessage(): ChatLog1 {
        return ChatCommonLog.of(
            messageId = messageId,
            roomId = roomId,
            senderId = senderId,
            timestamp = sendTime,
            seqNumber = seqNumber,
            page = page,
            text = message
        )
    }
}