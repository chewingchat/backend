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
//  chatFriendEntity로 이동 할게요 -> chatRoomEntity에는 모든 사용자가 봤을때 같은 내용이 들어가 있어야 해요
//    @Column(name = "favorite", nullable = false)
//    val favorite: Boolean,

    private val groupChatRoom: Boolean,

    private val createdAt: LocalDateTime = LocalDateTime.now(),

//  이거 chatSequence에서 번호 가져오고 chatFriendEntity에서 unread 가져와서 뺄셈 하면 될거 같아요
//    @Column(name = "total_unread_message", nullable = false)
//    val totalUnReadMessage: Int,

    // latestPage는 sequnence에서 가져와서 page계산 진행 할게요
//    @Transient
//    var latestPage: Int? = null,
//  chatFriendEntity로 이동 할게요 -> chatRoomEntity에는 모든
    //  사용자가 봤을때 같은 내용이 들어가 있어야 해요
//  @Transient
//  var readSeqNumber: Int? = null
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