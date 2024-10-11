import jakarta.persistence.*
import org.chewing.v1.model.chat.ChatFriendInfo
import java.util.*
import kotlin.jvm.Transient

@Entity
@Table(name = "chat_friends")
data class ChatFriendEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val friendId: Int,
    val favorite: Boolean,
    val chatRoomId: String,
    @Transient
    var readSeqNumber: Int
) {
    // ChatFriendEntity -> ChatFriend 변환 메서드
    fun toChatFriend(): ChatFriendInfo {
        return ChatFriendInfo(
            friendId = this.friendId,
            chatRoomId = this.chatRoomId,
            readSeqNumber = this.readSeqNumber
        )
    }
}