package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.chewing.v1.model.UserName
import org.chewing.v1.model.friend.FriendInfo
import org.hibernate.annotations.DynamicInsert


@DynamicInsert
@Entity
@Table(name = "friend", schema = "chewing")
internal class FriendJpaEntity(
    @EmbeddedId
    val id: FriendId,
    var favorite: Boolean,
    var friendFirstName: String,
    var friendLastName: String,
) : BaseEntity() {
    companion object {
        fun generate(user: User, friendName: UserName, targetUser: User): FriendJpaEntity {
            return FriendJpaEntity(
                id = FriendId(user.userId, targetUser.userId),
                favorite = false,
                friendFirstName = friendName.firstName(),
                friendLastName = friendName.lastName(),
            )
        }
    }

    fun updateFavorite(favorite: Boolean) {
        this.favorite = favorite
    }

    fun updateName(friendName: UserName) {
        this.friendFirstName = friendName.firstName()
        this.friendLastName = friendName.lastName()
    }

    fun toFriendInfo(): FriendInfo {
        return FriendInfo.of(
            friendId = id.friendId,
            friendName = UserName.of(friendFirstName, friendLastName),
            isFavorite = favorite,
        )
    }
}