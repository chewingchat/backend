package org.chewing.v1.model.chat.message

import org.chewing.v1.model.chat.MessageType
import org.chewing.v1.model.chat.room.ChatNumber
import java.time.LocalDateTime

class ChatInviteMessage private constructor(
    val messageId: String,
    override val chatRoomId: String,
    override val senderId: String,
    override val timestamp: LocalDateTime,
    override val number: ChatNumber,
    val targetUserId: String
) : ChatMessage() {
    override val type: MessageType = MessageType.INVITE

    companion object {
        fun of(
            messageId: String,
            chatRoomId: String,
            senderId: String,
            timestamp: LocalDateTime,
            number: ChatNumber,
            targetUserId: String
        ): ChatInviteMessage {
            return ChatInviteMessage(
                messageId = messageId,
                chatRoomId = chatRoomId,
                senderId = senderId,
                timestamp = timestamp,
                number = number,
                targetUserId = targetUserId
            )
        }
    }
}
