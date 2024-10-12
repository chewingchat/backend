package org.chewing.v1.model.chat.log

import org.chewing.v1.model.chat.MessageType
import java.time.LocalDateTime

class ChatCommonLog private constructor(
    override val messageId: String,
    override val roomId: String,
    override val senderId: String,
    override val timestamp: LocalDateTime,
    override val seqNumber: Int,
    override val page: Int,
    val text: String
) : ChatLog1() {
    override val type: MessageType = MessageType.CHAT

    companion object {
        fun of(
            messageId: String,
            roomId: String,
            senderId: String,
            text: String,
            seqNumber: Int,
            page: Int,
            timestamp: LocalDateTime
        ): ChatCommonLog {
            return ChatCommonLog(
                messageId = messageId,
                roomId = roomId,
                senderId = senderId,
                text = text,
                seqNumber = seqNumber,
                page = page,
                timestamp = timestamp
            )
        }
    }
}