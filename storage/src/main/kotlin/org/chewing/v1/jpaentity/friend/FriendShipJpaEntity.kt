package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.friend.FriendShip
import org.hibernate.annotations.DynamicInsert


@DynamicInsert
@Entity
@Table(name = "friend_ship", schema = "chewing")
internal class FriendShipJpaEntity(
    @EmbeddedId
    private val id: FriendShipId,
    private var favorite: Boolean,
    private var friendFirstName: String,
    private var friendLastName: String,
    @Enumerated(EnumType.STRING)
    private var type: AccessStatus
) : BaseEntity() {
    companion object {
        fun generate(user: User, friendName: UserName, targetUser: User): FriendShipJpaEntity {
            return FriendShipJpaEntity(
                id = FriendShipId(user.userId, targetUser.userId),
                favorite = false,
                friendFirstName = friendName.firstName(),
                friendLastName = friendName.lastName(),
                type = AccessStatus.ACCESS
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

    fun toFriendShip(): FriendShip {
        return FriendShip.of(
            friendId = id.friendId,
            friendName = UserName.of(friendFirstName, friendLastName),
            isFavorite = favorite,
            type = type
        )
    }

    fun updateBlock() {
        this.type = AccessStatus.BLOCK
    }

    fun updateBlocked() {
        this.type = AccessStatus.BLOCKED
    }
    fun updateDelete() {
        this.type = AccessStatus.DELETE
    }

    fun getId(): FriendShipId {
        return id
    }
}