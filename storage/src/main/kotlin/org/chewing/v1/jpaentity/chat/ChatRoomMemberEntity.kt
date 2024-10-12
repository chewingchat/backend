package org.chewing.v1.jpaentity.chat

import jakarta.persistence.*
import org.chewing.v1.model.chat.ChatRoomMemberInfo
import java.util.*
import kotlin.jvm.Transient

@Entity
@Table(name = "chat_room_member", schema = "chewing")
data class ChatRoomMemberEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    var favorite: Boolean,
    val chatRoomId: String,
    var readSeqNumber: Int,
    var isDeleted: Boolean = false
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
        return ChatRoomMemberInfo(
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
        this.isDeleted = true
    }
    fun updateUnDelete() {
        this.isDeleted = false
    }
}