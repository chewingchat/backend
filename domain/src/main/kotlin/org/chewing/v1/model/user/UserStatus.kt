package org.chewing.v1.model.user

class UserStatus private constructor(
    val statusId: String,
    val message: String,
    val emoji: String,
    val userId: String,
    val isSelected: Boolean,
) {
    companion object {
        fun of(
            statusId: String,
            statusMessage: String,
            emoji: String,
            userId: String,
            isSelected: Boolean
        ): UserStatus {
            return UserStatus(statusId, statusMessage, emoji, userId, isSelected)
        }
        fun default(userId: String): UserStatus {
            return UserStatus("none", "none", "none", userId, true)
        }
    }
}
