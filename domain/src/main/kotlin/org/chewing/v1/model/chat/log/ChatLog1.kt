package org.chewing.v1.model.chat.log

import org.chewing.v1.model.chat.MessageType
import java.time.LocalDateTime

sealed class ChatLog1 {
    abstract val type: MessageType
    abstract val messageId: String
    abstract val roomId: String
    abstract val senderId: String
    abstract val timestamp: LocalDateTime
    abstract val seqNumber: Int
    abstract val page: Int
}