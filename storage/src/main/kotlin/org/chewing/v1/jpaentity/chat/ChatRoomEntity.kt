import jakarta.persistence.*

import org.chewing.v1.model.chat.ChatRoom
import kotlin.jvm.Transient

@Entity
@Table(name = "chat_rooms")
data class ChatRoomEntity(
    @Id
    @Column(name = "chat_room_id")
    val chatRoomId: String,

    @Column(name = "favorite", nullable = false)
    val favorite: Boolean,

    @Column(name = "group_chat_room", nullable = false)
    val groupChatRoom: Boolean,

    @Column(name = "latest_message", nullable = false)
    val latestMessage: String,

    @Column(name = "latest_message_time", nullable = false)
    val latestMessageTime: String,

    @Column(name = "total_unread_message", nullable = false)
    val totalUnReadMessage: Int,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    val chatFriends: List<ChatFriendEntity>,

    @Transient
    var latestPage: Int? = null,

    @Transient
    var readSeqNumber: Int? = null
) {
    // ChatRoomEntity -> ChatRoom 변환 메서드
    fun toChatRoom(): ChatRoom {
        return ChatRoom(
            chatRoomId = this.chatRoomId,
            favorite = this.favorite,
            groupChatRoom = this.groupChatRoom,
            latestMessage = this.latestMessage,
            latestMessageTime = this.latestMessageTime,
            totalUnReadMessage = this.totalUnReadMessage,
            chatFriends = this.chatFriends.map { it.toChatFriend() },
            latestPage = this.latestPage ?: 0,
            readSeqNumber = this.readSeqNumber ?: 0
        )
    }
}