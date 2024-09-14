package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert


@DynamicInsert
@Entity
@Table(name = "friend", schema = "chewing")
class FriendJpaEntity(
    @EmbeddedId
    val id: FriendId,
    val favorite: Boolean,
    val friendFirstName: String,
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

    companion object {
        fun fromFriend(user: User, friend: Friend): FriendJpaEntity {
            return FriendJpaEntity(
                id = FriendId(userId = user.userId.value(), friendId = friend.friend.userId.value()),
                favorite = friend.isFavorite,
                friendFirstName = friend.name.firstName(),
                friendLastName = friend.name.lastName(),
                user = UserJpaEntity.fromUser(user),
                friend = UserJpaEntity.fromUser(friend.friend)
            )
        }
    }
}