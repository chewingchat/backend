package org.chewing.v1.repository.support

import org.chewing.v1.model.chat.message.ChatNormalMessage
import org.chewing.v1.model.chat.message.MessageType
import org.chewing.v1.model.chat.room.ChatNumber
import java.time.LocalDateTime

object ChatMessageProvider {
    fun buildNormalMessage(messageId: String, chatRoomId: String): ChatNormalMessage {
        return ChatNormalMessage.of(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = "sender",
            text = "text",
            number = ChatNumber.of(chatRoomId, 1, 1),
            timestamp = LocalDateTime.now(),
        )
    }
}