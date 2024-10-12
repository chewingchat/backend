package org.chewing.v1.dto.response.chat

import org.chewing.v1.dto.response.MediaResponse
import org.chewing.v1.model.chat.log.*
import java.time.LocalDateTime

sealed class ChatLogMessageResponse {
    data class Reply(
        val messageId: String,
        val type: String,
        val roomId: String,
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
        val roomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    data class Leave(
        val messageId: String,
        val roomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    data class Invite(
        val messageId: String,
        val roomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    data class File(
        val messageId: String,
        val type: String,
        val roomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val files: List<MediaResponse>
    ) : ChatLogMessageResponse()

    data class Message(
        val messageId: String,
        val type: String,
        val roomId: String,
        val senderId: String,
        val timestamp: LocalDateTime,
        val seqNumber: Int,
        val page: Int,
        val text: String
    ) : ChatLogMessageResponse()

    companion object {
        fun from(chatLog: ChatLog1): ChatLogMessageResponse {
            return when (chatLog) {
                is ChatReplyLog -> Reply(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    roomId = chatLog.roomId,
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
                is ChatDeleteLog -> Delete(
                    messageId = chatLog.messageId,
                    roomId = chatLog.roomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    text = chatLog.text
                )
                is ChatLeaveLog -> Leave(
                    messageId = chatLog.messageId,
                    roomId = chatLog.roomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    text = chatLog.text
                )
                is ChatInviteLog -> Invite(
                    messageId = chatLog.messageId,
                    roomId = chatLog.roomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    text = chatLog.text
                )
                is ChatFileLog -> File(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    roomId = chatLog.roomId,
                    senderId = chatLog.senderId,
                    timestamp = chatLog.timestamp,
                    seqNumber = chatLog.seqNumber,
                    page = chatLog.page,
                    files = chatLog.medias.map { MediaResponse.from(it) }
                )
                is ChatCommonLog -> Message(
                    messageId = chatLog.messageId,
                    type = chatLog.type.toString(),
                    roomId = chatLog.roomId,
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
