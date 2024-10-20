package org.chewing.v1.jpaentity.chat

import jakarta.persistence.*
import org.chewing.v1.model.chat.message.MessageType

import org.chewing.v1.model.chat.room.ChatRoomInfo
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "chatrooms", schema = "chewing")
internal class ChatRoomEntity(
    @Id
    private val chatRoomId: String = UUID.randomUUID().toString(),

    private val groupChatRoom: Boolean,

    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun generate(groupChatRoom: Boolean): ChatRoomEntity {
            return ChatRoomEntity(
                groupChatRoom = groupChatRoom,
            )
        }
    }

    // ChatRoomEntity -> ChatRoom 변환 메서드
    fun toChatRoomInfo(): ChatRoomInfo {
        return ChatRoomInfo.of(
            chatRoomId = this.chatRoomId,
            isGroup = this.groupChatRoom,
        )
    }

    fun toChatRoomId(): String {
        return this.chatRoomId
    }


}