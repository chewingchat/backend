package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.model.chat.MessageSendType
import org.chewing.v1.model.chat.MessageType
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
// 복합 인덱스 설정
@CompoundIndexes(
    CompoundIndex(name = "roomId_page_idx", def = "{'roomId': 1, 'page': 1}")
)
data class ChatMessageMongoEntity(
    val messageId: String,
    @Indexed  // 개별 인덱스 추가 가능
    val roomId: String,
    var type: MessageType,
    val senderId: String,
    val messageSendType: MessageSendType?,
    val parentMessageId: String?,
    val parentMessagePage: Int?,
    val parentSeqNumber: Int?,
    val seqNumber: Int?,
    val page: Int,
    val sendTime: LocalDateTime,
    val friends: List<ChatMessage.FriendSeqInfo>?
) {
    companion object {
        fun fromChatMessage(chatMessage: ChatMessage, page: Int): ChatMessageMongoEntity {
            return ChatMessageMongoEntity(
                messageId = chatMessage.messageId,
                roomId = chatMessage.roomId ?: "",
                type = chatMessage.type,
                senderId = chatMessage.sender,
                messageSendType = chatMessage.messageSendType,
                parentMessageId = chatMessage.parentMessageId,
                parentMessagePage = chatMessage.parentMessagePage,
                parentSeqNumber = chatMessage.parentSeqNumber,
                seqNumber = chatMessage.seqNumber,
                page = chatMessage.page,
                sendTime = chatMessage.timestamp ?: LocalDateTime.now(),
                friends = chatMessage.friends
            )
        }
    }
    fun toChatMessage(): ChatMessage {
        return ChatMessage(
            messageId = this.messageId,
            type = this.type,
            roomId = this.roomId,
            sender = this.senderId,
            messageSendType = this.messageSendType,
            parentMessageId = this.parentMessageId,
            parentMessagePage = this.parentMessagePage,
            parentSeqNumber = this.parentSeqNumber,
            timestamp = this.sendTime,
            friends = this.friends,
            seqNumber = this.seqNumber,
            page = this.page
        )
    }
    fun delete(){
        this.type = MessageType.DELETE
    }
}