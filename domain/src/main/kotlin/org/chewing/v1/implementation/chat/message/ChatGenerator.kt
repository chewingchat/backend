package org.chewing.v1.implementation.chat.message

import org.chewing.v1.model.chat.MessageType
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
        when (parentMessage) {
            is ChatCommonMessage -> {
                return ChatReplyMessage.of(
                    generateKey(chatRoomId),
                    chatRoomId = chatRoomId,
                    senderId = userId,
                    timestamp = LocalDateTime.now(),
                    number = number,
                    text = text,
                    parentMessageId = parentMessage.messageId,
                    parentMessagePage = parentMessage.number.page,
                    parentMessageText = parentMessage.text,
                    parentSeqNumber = parentMessage.number.sequenceNumber,
                    parentMessageType = parentMessage.type,
                    type = MessageType.REPLY
                )
            }

            is ChatReplyMessage -> {
                return ChatReplyMessage.of(
                    generateKey(chatRoomId),
                    chatRoomId = chatRoomId,
                    senderId = userId,
                    timestamp = LocalDateTime.now(),
                    number = number,
                    text = text,
                    parentMessageId = parentMessage.parentMessageId,
                    parentMessagePage = parentMessage.parentMessagePage,
                    parentMessageText = parentMessage.parentMessageText,
                    parentSeqNumber = parentMessage.parentSeqNumber,
                    parentMessageType = parentMessage.type,
                    type = MessageType.REPLY
                )
            }

            is ChatFileMessage -> {
                return ChatReplyMessage.of(
                    generateKey(chatRoomId),
                    chatRoomId = chatRoomId,
                    senderId = userId,
                    timestamp = LocalDateTime.now(),
                    number = number,
                    text = text,
                    parentMessageId = parentMessage.messageId,
                    parentMessagePage = parentMessage.number.page,
                    parentMessageText = parentMessage.medias[0].url,
                    parentSeqNumber = parentMessage.number.sequenceNumber,
                    parentMessageType = parentMessage.type,
                    type = MessageType.REPLY
                )
            }

            else -> {
                throw IllegalArgumentException("Invalid parent message type")
            }
        }
    }

    private fun generateKey(chatRoomId: String): String {
        return chatRoomId + UUID.randomUUID().toString()
    }
}