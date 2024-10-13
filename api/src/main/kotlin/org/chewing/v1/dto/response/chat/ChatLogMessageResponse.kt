package org.chewing.v1.dto.response.chat

import org.chewing.v1.dto.response.MediaResponse
import org.chewing.v1.model.chat.message.*
import java.time.LocalDateTime

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
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    data class Delete(
        val messageId: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
    ) : ChatLogMessageResponse()

    data class Leave(
        val messageId: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
    ) : ChatLogMessageResponse()

    data class Invite(
        val messageId: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
    ) : ChatLogMessageResponse()

    data class File(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val files: List<MediaResponse>
    ) : ChatLogMessageResponse()

    data class Message(
        val messageId: String,
        val type: String,
        val chatRoomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    companion object {
        fun from(chatLog: ChatMessage): ChatLogMessageResponse {
            return when (chatLog) {
                is ChatReplyMessage -> Reply(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    parentMessageId = chatLog.parentMessageId,
                    parentMessagePage = chatLog.parentMessagePage,
                    parentSeqNumber = chatLog.parentSeqNumber,
                    parentMessageText = chatLog.parentMessageText,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    text = chatLog.text
                )
                is ChatDeleteMessage -> Delete(
                    messageId = chatLog.messageId,
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                )
                is ChatLeaveMessage -> Leave(
                    messageId = chatLog.messageId,
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                )
                is ChatInviteMessage -> Invite(
                    messageId = chatLog.messageId,
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                )
                is ChatFileMessage -> File(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    files = chatLog.medias.map { MediaResponse.from(it) }
                )
                is ChatCommonMessage -> Message(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    chatRoomId = chatLog.chatRoomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    text = chatLog.text
                )
            }
        }
    }
}
