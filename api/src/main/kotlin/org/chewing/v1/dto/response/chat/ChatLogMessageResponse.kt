package org.chewing.v1.dto.response.chat

import org.chewing.v1.dto.response.MediaResponse
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.chat.log.*
import org.chewing.v1.model.chat.message.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class ChatLogMessageResponse {
    data class Reply(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val parentMessageId: String,
        val parentMessagePage: Int,
        val parentSeqNumber: Int,
        val parentMessageText: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    data class Delete(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
    ) : ChatLogMessageResponse()

    data class Leave(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
    ) : ChatLogMessageResponse()

    data class Invite(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
    ) : ChatLogMessageResponse()

    data class File(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
        val files: List<MediaResponse>
    ) : ChatLogMessageResponse()

    data class Message(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    data class Bomb(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: String,
        val seqNumber: Int,
        val page: Int,
        val expiredAt: String,
        val text: String
    ) : ChatLogMessageResponse()

    companion object {
        fun from(chatLog: ChatLog): ChatLogMessageResponse {
            val formattedTime = chatLog.timestamp.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            val formattedExpiredTime = chatLog.timestamp.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))

            return when (chatLog) {
                is ChatReplyLog -> Reply(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    parentMessageId = chatLog.parentMessageId,
                    parentMessagePage = chatLog.parentMessagePage,
                    parentSeqNumber = chatLog.parentSeqNumber,
                    parentMessageText = chatLog.parentMessageText,
                    timestamp = formattedTime,
                    seqNumber = chatLog.number.sequenceNumber,
                    page = chatLog.number.page,
                    text = chatLog.text
                )
                is ChatLeaveLog -> Leave(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatLog.number.sequenceNumber,
                    page = chatLog.number.page,
                )
                is ChatInviteLog -> Invite(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatLog.number.sequenceNumber,
                    page = chatLog.number.page,
                )
                is ChatFileLog -> File(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatLog.number.sequenceNumber,
                    page = chatLog.number.page,
                    files = chatLog.medias.map { MediaResponse.from(it) }
                )
                is ChatNormalLog -> Message(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatLog.number.sequenceNumber,
                    page = chatLog.number.page,
                    text = chatLog.text
                )
                is ChatBombLog -> Bomb(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatLog.number.sequenceNumber,
                    page = chatLog.number.page,
                    expiredAt = formattedExpiredTime,
                    text = chatLog.text
                )
            }
        }
    }
}
