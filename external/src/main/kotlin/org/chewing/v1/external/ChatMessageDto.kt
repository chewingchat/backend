package org.chewing.v1.external

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.chewing.v1.model.chat.message.*
import java.time.format.DateTimeFormatter

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatMessageDto.Reply::class, name = "Reply"),
    JsonSubTypes.Type(value = ChatMessageDto.Delete::class, name = "Delete"),
    JsonSubTypes.Type(value = ChatMessageDto.Leave::class, name = "Leave"),
    JsonSubTypes.Type(value = ChatMessageDto.Invite::class, name = "Invite"),
    JsonSubTypes.Type(value = ChatMessageDto.File::class, name = "File"),
    JsonSubTypes.Type(value = ChatMessageDto.Normal::class, name = "Message"),
    JsonSubTypes.Type(value = ChatMessageDto.Bomb::class, name = "Bomb"),
    JsonSubTypes.Type(value = ChatMessageDto.Read::class, name = "Read")
)
sealed class ChatMessageDto {
    data class Reply @JsonCreator constructor(
        @JsonProperty("messageId") val messageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("parentMessageId") val parentMessageId: String,
        @JsonProperty("parentMessagePage") val parentMessagePage: Int,
        @JsonProperty("parentSeqNumber") val parentSeqNumber: Int,
        @JsonProperty("parentMessageText") val parentMessageText: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int,
        @JsonProperty("text") val text: String
    ) : ChatMessageDto()

    data class Delete @JsonCreator constructor(
        @JsonProperty("targetMessageId") val targetMessageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int
    ) : ChatMessageDto()

    data class Leave @JsonCreator constructor(
        @JsonProperty("messageId") val messageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int
    ) : ChatMessageDto()

    data class Invite @JsonCreator constructor(
        @JsonProperty("messageId") val messageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int
    ) : ChatMessageDto()

    data class File @JsonCreator constructor(
        @JsonProperty("messageId") val messageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int,
        @JsonProperty("files") val files: List<MediaDto>
    ) : ChatMessageDto()

    data class Normal @JsonCreator constructor(
        @JsonProperty("messageId") val messageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int,
        @JsonProperty("text") val text: String
    ) : ChatMessageDto()

    data class Bomb @JsonCreator constructor(
        @JsonProperty("messageId") val messageId: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int,
        @JsonProperty("expiredAt") val expiredAt: String,
        @JsonProperty("text") val text: String
    ) : ChatMessageDto()

    data class Read @JsonCreator constructor(
        @JsonProperty("type") val type: String,
        @JsonProperty("chatRoomId") val chatRoomId: String,
        @JsonProperty("senderId") val senderId: String,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("seqNumber") val seqNumber: Int,
        @JsonProperty("page") val page: Int
    ) : ChatMessageDto()

    companion object {
        fun from(chatMessage: ChatMessage): ChatMessageDto {
            val formattedTime = chatMessage.timestamp.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            val formattedExpiredTime = chatMessage.timestamp.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return when (chatMessage) {
                is ChatReplyMessage -> Reply(
                    messageId = chatMessage.messageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    parentMessageId = chatMessage.parentMessageId,
                    parentMessagePage = chatMessage.parentMessagePage,
                    parentSeqNumber = chatMessage.parentSeqNumber,
                    parentMessageText = chatMessage.parentMessageText,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page,
                    text = chatMessage.text
                )
                is ChatLeaveMessage -> Leave(
                    messageId = chatMessage.messageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page
                )
                is ChatInviteMessage -> Invite(
                    messageId = chatMessage.messageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page
                )
                is ChatFileMessage -> File(
                    messageId = chatMessage.messageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page,
                    files = chatMessage.medias.map { MediaDto.from(it) }
                )
                is ChatNormalMessage -> Normal(
                    messageId = chatMessage.messageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page,
                    text = chatMessage.text
                )
                is ChatBombMessage -> Bomb(
                    messageId = chatMessage.messageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page,
                    expiredAt = formattedExpiredTime,
                    text = chatMessage.text
                )
                is ChatReadMessage -> Read(
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page
                )

                is ChatDeleteMessage -> Delete(
                    targetMessageId = chatMessage.targetMessageId,
                    type = chatMessage.type.toString().lowercase(),
                    chatRoomId = chatMessage.chatRoomId,
                    senderId = chatMessage.senderId,
                    timestamp = formattedTime,
                    seqNumber = chatMessage.number.sequenceNumber,
                    page = chatMessage.number.page
                )
            }
        }
    }
}
