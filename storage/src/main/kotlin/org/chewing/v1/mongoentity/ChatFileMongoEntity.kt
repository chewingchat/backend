package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.log.ChatFileLog
import org.chewing.v1.model.chat.log.ChatLog1
import org.chewing.v1.model.chat.room.ChatRoomNumber
import org.chewing.v1.model.media.Media
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime


@Document(collection = "chat_messages")
internal class ChatFileMongoEntity(
    roomId: String,
    senderId: String,
    seqNumber: Int,
    page: Int,
    sendTime: LocalDateTime,
    val medias: List<Media>
) : ChatMessageMongoEntity(
    roomId = roomId,
    senderId = senderId,
    type = MessageType.FILE,
    seqNumber = seqNumber,
    page = page,
    sendTime = sendTime
) {
    companion object {
        fun fromFileMessage(
            medias: List<Media>,
            roomId: String,
            senderId: String,
            number: ChatRoomNumber
        ): ChatFileMongoEntity {
            return ChatFileMongoEntity(
                roomId = roomId,
                senderId = senderId,
                seqNumber = number.sequenceNumber,
                page = number.page,
                sendTime = LocalDateTime.now(),
                medias = medias
            )
        }
    }

    override fun toChatMessage(): ChatLog1 {
        return ChatFileLog.of(
            messageId = messageId,
            roomId = roomId,
            senderId = senderId,
            timestamp = sendTime,
            seqNumber = seqNumber,
            page = page,
            medias = medias
        )
    }
}