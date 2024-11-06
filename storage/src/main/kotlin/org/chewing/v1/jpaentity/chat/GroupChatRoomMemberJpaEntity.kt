package org.chewing.v1.jpaentity.chat

import jakarta.persistence.*
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatNumber

@Entity
@Table(
    name = "chat_room_member",
    schema = "chewing",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_chat_room_id", columnList = "chat_room_id"),
    ]
)
internal data class GroupChatRoomMemberJpaEntity(
    @EmbeddedId
    private val id: ChatRoomMemberId,
    private var favorite: Boolean,
    private var readSeqNumber: Int,
    @Version
    private var version: Long? = 0,
    private var startSeqNumber: Int,
) {
    companion object {
        fun generate(userId: String, chatRoomId: String, number: ChatNumber): GroupChatRoomMemberJpaEntity {
            return GroupChatRoomMemberJpaEntity(
                id = ChatRoomMemberId(chatRoomId, userId),
                favorite = false,
                readSeqNumber = number.sequenceNumber,
                startSeqNumber = number.sequenceNumber
            )
        }
    }

    fun toRoomMember(): ChatRoomMemberInfo {
        return ChatRoomMemberInfo.of(
            memberId = this.id.userId,
            chatRoomId = this.id.chatRoomId,
            readSeqNumber = this.readSeqNumber,
            favorite = this.favorite,
            startSeqNumber = this.startSeqNumber
        )
    }

    fun updateFavorite(favorite: Boolean) {
        this.favorite = favorite
    }

    fun updateRead(number: ChatNumber) {
        this.readSeqNumber = number.sequenceNumber
    }

    fun chatRoomId(): String {
        return id.chatRoomId
    }
}
