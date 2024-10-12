package org.chewing.v1.model.chat.log

import org.chewing.v1.model.chat.MessageType
import java.time.LocalDateTime

class ChatLeaveLog private constructor(
    override val messageId: String,
    override val roomId: String,
    override val senderId: String,
    override val timestamp: LocalDateTime,
    override val seqNumber: Int,
    override val page: Int,
    val text: String
) : ChatLog1() {
    override val type: MessageType = MessageType.LEAVE

    companion object {
        fun of(
            messageId: String,
            roomId: String,
            senderId: String,
            timestamp: LocalDateTime,
            seqNumber: Int,
            page: Int,
            text: String
        ): ChatLeaveLog {
            return ChatLeaveLog(
                messageId = messageId,
                roomId = roomId,
                senderId = senderId,
                timestamp = timestamp,
                seqNumber = seqNumber,
                page = page,
                text = text
            )
        }
    }
}