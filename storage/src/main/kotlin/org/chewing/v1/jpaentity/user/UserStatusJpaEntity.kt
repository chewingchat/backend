package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.model.user.UserStatus
import java.util.*

@Entity
@Table(
    name = "user_status",
    schema = "chewing",
    indexes = [Index(name = "idx_user_status_selected", columnList = "selected")]
)
internal class UserStatusJpaEntity(
    @Id
    private val statusId: String = UUID.randomUUID().toString(),

    private val message: String,

    private val emoji: String,

    private val userId: String,

    private var selected: Boolean
) {
    companion object {
        fun generate(userId: String, message: String, emoji: String): UserStatusJpaEntity {
            return UserStatusJpaEntity(
                message = message,
                emoji = emoji,
                userId = userId,
                selected = false
            )
        }
    }

    fun toUserStatus(): UserStatus {
        return UserStatus.of(statusId, message, userId, emoji, selected)
    }

    fun updateSelectedFalse() {
        this.selected = false
    }

    fun updateSelectedTrue() {
        this.selected = true
    }
}
