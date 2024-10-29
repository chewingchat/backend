package org.chewing.v1.jpaentity.chat

import jakarta.persistence.*
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatNumber
import java.util.*

@Entity
@Table(name = "chat_room_member", schema = "chewing")
internal data class ChatRoomMemberEntity(
    @Id
    private val id: String = UUID.randomUUID().toString(),
    private val userId: String,
    private var favorite: Boolean,
    private val chatRoomId: String,
    private var readSeqNumber: Int,
    @Version
    private var version: Long? = 0,
    private var deleted: Boolean = false
) {
    companion object{
        fun generate(userId: String, chatRoomId: String): ChatRoomMemberEntity {
            return ChatRoomMemberEntity(
                userId = userId,
                favorite = false,
                chatRoomId = chatRoomId,
                readSeqNumber = 0
            )
        }
    }
    // ChatFriendEntity -> ChatFriend 변환 메서드
    fun toRoomMember(): ChatRoomMemberInfo {
        return ChatRoomMemberInfo.of(
            memberId = this.userId,
            chatRoomId = this.chatRoomId,
            readSeqNumber = this.readSeqNumber,
            favorite = this.favorite
        )
    }
    fun updateFavorite(favorite: Boolean) {
        this.favorite = favorite
    }
    fun updateDelete() {
        this.deleted = true
    }
    fun updateUnDelete() {
        this.deleted = false
    }
    fun updateRead(number: ChatNumber){
        this.readSeqNumber = number.sequenceNumber
    }

    fun chatRoomId(): String {
        return chatRoomId
    }
}