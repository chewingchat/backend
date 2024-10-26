package org.chewing.v1.model.chat.log

import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.message.MessageType
import org.chewing.v1.model.chat.room.ChatNumber
import java.time.LocalDateTime

class ChatInviteLog private constructor(
    override val messageId: String,
    override val chatRoomId: String,
    override val senderId: String,
    override val timestamp: LocalDateTime,
    override val number: ChatNumber,
    override val type: ChatLogType,
    val targetUserId: String
) : ChatLog() {

    companion object {
        fun of(
            messageId: String,
            chatRoomId: String,
            senderId: String,
            timestamp: LocalDateTime,
            number: ChatNumber,
            targetUserId: String,
            type: ChatLogType
        ): ChatInviteLog {
            return ChatInviteLog(
                messageId = messageId,
                chatRoomId = chatRoomId,
                senderId = senderId,
                timestamp = timestamp,
                number = number,
                targetUserId = targetUserId,
                type = type
            )
        }
    }
}
