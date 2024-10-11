import jakarta.persistence.*
import org.chewing.v1.model.chat.ChatFriend

@Entity
@Table(name = "chat_friends")
data class ChatFriendEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "friend_id", nullable = false)
    val friendId: Int,

    @Column(name = "friend_first_name", nullable = false)
    val friendFirstName: String,

    @Column(name = "friend_last_name", nullable = false)
    val friendLastName: String,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String
) {
    // ChatFriendEntity -> ChatFriend 변환 메서드
    fun toChatFriend(): ChatFriend {
        return ChatFriend(
            friendId = this.friendId,
            friendFirstName = this.friendFirstName,
            friendLastName = this.friendLastName,
            imageUrl = this.imageUrl
        )
    }
}