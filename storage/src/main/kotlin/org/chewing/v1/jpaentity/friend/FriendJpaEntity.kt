package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.common.BaseEntity
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

    @Column(name = "friend_first_name", nullable = false)
    val friendFirstName: String,

    @Column(name = "friend_last_name", nullable = false)
    val friendLastName: String,

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
            friendFirstName = friendFirstName,
            friendLastName = friendLastName
        )
    }

    fun toUser(): User {
        return user.toUser()
    }

    companion object {
        fun fromFriend(user: User, friend: Friend): FriendJpaEntity {
            return FriendJpaEntity(
                id = FriendId(userId = user.userId.value(), friendId = friend.friend.userId.value()),
                favorite = friend.isFavorite,
                friendFirstName = friend.friendName.firstName(),
                friendLastName = friend.friendName.lastName(),
                user = UserJpaEntity.fromUser(user),
                friend = UserJpaEntity.fromUser(friend.friend)
            )
        }
    }
}