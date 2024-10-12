package org.chewing.v1.mongoentity

import org.chewing.v1.model.chat.*
import org.chewing.v1.model.chat.log.*
import org.chewing.v1.model.chat.room.ChatRoomNumber
import org.chewing.v1.model.media.Media
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "chat_messages")
// 복합 인덱스 설정
@CompoundIndexes(
    CompoundIndex(name = "roomId_page_idx", def = "{'roomId': 1, 'page': 1}")
)
internal sealed class ChatMessageMongoEntity(
    @Id
    protected val messageId: String = UUID.randomUUID().toString(),
    protected val roomId: String,
    protected var type: MessageType,
    protected val senderId: String,
    protected val seqNumber: Int,
    protected val page: Int,
    protected val sendTime: LocalDateTime,
) {

    abstract fun toChatMessage(): ChatLog1

    fun delete() {
        this.type = MessageType.DELETE
    }
}