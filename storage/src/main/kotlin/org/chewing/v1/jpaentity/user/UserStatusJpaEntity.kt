package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import java.util.*

import jakarta.persistence.*
import org.chewing.v1.model.User
import org.chewing.v1.model.media.Image
import java.util.*

@Entity
@Table(
    name = "user_status",
    schema = "chewing",
    indexes = [Index(name = "idx_user_status_selected", columnList = "selected")]
)
class UserStatusJpaEntity(
    @Id
    val statusId: String = UUID.randomUUID().toString(),

    val statusMessage: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "emoticon_id", nullable = false)
    val emoticon: EmoticonJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,

    val selected: Boolean = false
) {
    companion object {
        fun fromUserStatus(user: User, status: User.UserStatus): UserStatusJpaEntity {
            return UserStatusJpaEntity(
                statusId = status.statusId,
                statusMessage = status.statusMessage,
                emoticon = EmoticonJpaEntity.fromEmoticon(status.emoticon),
                user = UserJpaEntity.fromUser(user),
                selected = false
            )
        }
    }
    fun toUserWithStatusAndEmoticon(): User {
        return User.of(
            this.user.userId,
            this.user.userFirstName,
            this.user.userLastName,
            this.user.birth,
            Image.of(this.user.pictureUrl, 0),
            Image.of(this.user.backgroundPictureUrl, 0),
            this.statusId,
            this.emoticon.toEmoticon(),
            this.statusMessage
        )
    }
    fun toUserStatus(): User.UserStatus {
        return User.UserStatus.of(this.statusId, this.statusMessage, this.emoticon.toEmoticon())
    }
}
