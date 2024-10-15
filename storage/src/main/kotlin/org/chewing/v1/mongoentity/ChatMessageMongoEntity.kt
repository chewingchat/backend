package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.*
import org.chewing.v1.model.chat.message.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_messages")
// 복합 인덱스 설정
@CompoundIndexes(
    CompoundIndex(name = "chatRoomId_page_idx", def = "{'chatRoomId': 1, 'page': 1}"),
    CompoundIndex(name = "chatRoomId_seqNumber_idx", def = "{'chatRoomId': 1, 'seqNumber': 1}")
)
internal sealed class ChatMessageMongoEntity(
    @Id
    protected val messageId: String,
    protected val chatRoomId: String,
    protected var type: MessageType,
    protected val senderId: String,
    protected val seqNumber: Int,
    protected val page: Int,
    protected val sendTime: LocalDateTime,
) {

    abstract fun toChatMessage(): ChatMessage

    fun delete() {
        this.type = MessageType.DELETE
    }
}