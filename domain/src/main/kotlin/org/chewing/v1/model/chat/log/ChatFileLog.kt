package org.chewing.v1.model.chat.log

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.media.Media
import java.time.LocalDateTime

class ChatFileLog private constructor(
    override val messageId: String,
    override val roomId: String,
    override val senderId: String,
    override val timestamp: LocalDateTime,
    override val seqNumber: Int,
    override val page: Int,
    val medias: List<Media>
) : ChatLog1() {
    override val type: MessageType = MessageType.FILE

    companion object {
        fun of(
            messageId: String,
            roomId: String,
            senderId: String,
            medias: List<Media>,
            timestamp: LocalDateTime,
            seqNumber: Int,
            page: Int
        ): ChatFileLog {
            return ChatFileLog(
                messageId = messageId,
                roomId = roomId,
                senderId = senderId,
                medias = medias,
                timestamp = timestamp,
                seqNumber = seqNumber,
                page = page
            )
        }
    }
}