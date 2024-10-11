import jakarta.persistence.*

import org.chewing.v1.model.chat.ChatRoomInfo

@Entity
@Table(name = "chat_rooms", schema = "chewing")
data class ChatRoomEntity(
    @Id
    @Column(name = "chat_room_id")
    val chatRoomId: String,
//  chatFriendEntity로 이동 할게요 -> chatRoomEntity에는 모든 사용자가 봤을때 같은 내용이 들어가 있어야 해요
//    @Column(name = "favorite", nullable = false)
//    val favorite: Boolean,

    @Column(name = "group_chat_room", nullable = false)
    val groupChatRoom: Boolean,
    //chatSequence 에서 가져와야 할거 같아요 -> mysql에서 하면 동기화 문제 생길 것 같아요
//    @Column(name = "latest_message", nullable = false)
//    val latestMessage: String,
//
//    @Column(name = "latest_message_time", nullable = false)
//    val latestMessageTime: String,

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
    // ChatRoomEntity -> ChatRoom 변환 메서드
    fun toChatRoomInfo(): ChatRoomInfo {
        return ChatRoomInfo.of(
            chatRoomId = this.chatRoomId,
            isGroupChatRoom = this.groupChatRoom
        )
    }
}