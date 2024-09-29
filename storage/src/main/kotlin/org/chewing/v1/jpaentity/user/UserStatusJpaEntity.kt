package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import java.util.*

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.user.StatusInfo

@Entity
@Table(
    name = "user_status",
    schema = "chewing",
    indexes = [Index(name = "idx_user_status_selected", columnList = "selected")]
)
internal class UserStatusJpaEntity(
    @Id
    val statusId: String = UUID.randomUUID().toString(),

    val statusMessage: String,

    val emoticonId: String,

    val userId: String,

    val selected: Boolean = false
) {
    companion object {
        fun generate(user: User, status: UserStatus): UserStatusJpaEntity {
            return UserStatusJpaEntity(
                statusId = status.statusId,
                statusMessage = status.message,
                emoticonId = status.emoticon.id,
                userId = user.userId,
                selected = false
            )
        }
    }

    fun toUserStatusInfo(): StatusInfo {
        return StatusInfo.of(statusId, statusMessage, userId, selected, emoticonId)
    }
}
