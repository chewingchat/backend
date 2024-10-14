package org.chewing.v1.model.chat.message

import org.chewing.v1.model.chat.message.MessageType
import org.chewing.v1.model.chat.room.ChatNumber
import java.time.LocalDateTime

class ChatCommonMessage private constructor(
    val messageId: String,
    override val chatRoomId: String,
    override val senderId: String,
    override val timestamp: LocalDateTime,
    override val number: ChatNumber,
    override val type: MessageType,
    val text: String
) : ChatMessage() {

    companion object {
        fun of(
            messageId: String,
            chatRoomId: String,
            senderId: String,
            text: String,
            number: ChatNumber,
            timestamp: LocalDateTime,
            type: MessageType
        ): ChatCommonMessage {
            return ChatCommonMessage(
                messageId = messageId,
                chatRoomId = chatRoomId,
                senderId = senderId,
                text = text,
                number = number,
                timestamp = timestamp,
                type = type
            )
        }
    }
}