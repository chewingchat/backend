package org.chewing.v1.model.chat.log

import org.chewing.v1.model.chat.MessageType
import java.time.LocalDateTime

class ChatReplyLog private constructor(
    override val messageId: String,
    override val roomId: String,
    override val senderId: String,
    val text: String,
    val parentMessageId: String,
    val parentMessagePage: Int,
    val parentMessageText: String,
    val parentSeqNumber: Int,
    override val timestamp: LocalDateTime,
    override val seqNumber: Int,
    override val page: Int
) : ChatLog1() {
    override val type: MessageType
        get() = MessageType.REPLY

    companion object {
        fun of(
            messageId: String,
            roomId: String,
            senderId: String,
            parentMessageId: String,
            parentMessagePage: Int,
            parentSeqNumber: Int,
            timestamp: LocalDateTime,
            seqNumber: Int,
            page: Int,
            text: String,
            parentMessageText: String
        ): ChatReplyLog {
            return ChatReplyLog(
                messageId = messageId,
                roomId = roomId,
                senderId = senderId,
                parentMessageId = parentMessageId,
                parentMessagePage = parentMessagePage,
                parentSeqNumber = parentSeqNumber,
                timestamp = timestamp,
                seqNumber = seqNumber,
                page = page,
                text = text,
                parentMessageText = parentMessageText
            )
        }
    }
}