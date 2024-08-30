package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.common.BaseEntity
import org.chewing.v1.model.Friend
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert

@DynamicInsert
@Entity
@Table(name = "friend", schema = "chewing")
class FriendJpaEntity(
    @EmbeddedId
    val id: FriendId,

    @Column(name = "favorite", nullable = false)
    val favorite: Boolean,

    @Column(name = "friend_name", nullable = false)
    val friendName: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val user: UserJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("friendId")
    @JoinColumn(name = "friend_id", insertable = false, updatable = false)
    val friend: UserJpaEntity
) : BaseEntity() {

    fun toFriend(): Friend {
        return Friend.of(
            friend = friend.toUser(),
            favorite = favorite,
            friendName = friendName
        )
    }

    fun toUser(): User {
        return user.toUser()
    }

    companion object {
        fun fromFriend(user: User, friend: Friend): FriendJpaEntity {
            return FriendJpaEntity(
                id = FriendId(userId = user.userId.value(), friendId = friend.friend.userId.value()),
                favorite = friend.favorite,
                friendName = friend.friendName,
                user = UserJpaEntity.fromUser(user),
                friend = UserJpaEntity.fromUser(friend.friend)
            )
        }
    }
}