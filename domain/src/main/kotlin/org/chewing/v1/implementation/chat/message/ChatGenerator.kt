package org.chewing.v1.implementation.chat.message

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.chat.message.MessageType
import org.chewing.v1.model.chat.message.*
import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class ChatGenerator {
    fun generateReadMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber
    ): ChatReadMessage {
        return ChatReadMessage.of(
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number
        )
    }

    fun generateDeleteMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber,
        messageId: String
    ): ChatDeleteMessage {
        return ChatDeleteMessage.of(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number
        )
    }

    fun generateCommonMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber,
        text: String
    ): ChatCommonMessage {
        return ChatCommonMessage.of(
            generateKey(chatRoomId),
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number,
            text = text,
            type = MessageType.CHAT
        )
    }

    fun generateInviteMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber,
        targetUserId: String
    ): ChatInviteMessage {
        return ChatInviteMessage.of(
            generateKey(chatRoomId),
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number,
            targetUserId = targetUserId
        )
    }

    fun generateLeaveMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber,
    ): ChatLeaveMessage {
        return ChatLeaveMessage.of(
            generateKey(chatRoomId),
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number,
        )
    }

    fun generateFileMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber,
        medias: List<Media>
    ): ChatFileMessage {
        return ChatFileMessage.of(
            generateKey(chatRoomId),
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number,
            medias = medias
        )
    }

    fun generateReplyMessage(
        chatRoomId: String,
        userId: String,
        number: ChatNumber,
        text: String,
        parentMessage: ChatMessage
    ): ChatReplyMessage {
        val (parentMessageId, parentMessageText) = when (parentMessage) {
            is ChatCommonMessage -> Pair(parentMessage.messageId, parentMessage.text)
            is ChatReplyMessage -> Pair(parentMessage.messageId, parentMessage.text)
            is ChatFileMessage -> Pair(parentMessage.messageId, parentMessage.medias[0].url)
            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        return ChatReplyMessage.of(
            generateKey(chatRoomId),
            chatRoomId = chatRoomId,
            senderId = userId,
            timestamp = LocalDateTime.now(),
            number = number,
            text = text,
            parentMessageId = parentMessageId,
            parentMessagePage = parentMessage.number.page,
            parentMessageText = parentMessageText,
            parentSeqNumber = parentMessage.number.sequenceNumber,
            parentMessageType = parentMessage.type,
            type = MessageType.REPLY
        )
    }

    private fun generateKey(chatRoomId: String): String {
        return chatRoomId + UUID.randomUUID().toString()
    }
}