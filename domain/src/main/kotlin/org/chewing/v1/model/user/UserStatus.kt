package org.chewing.v1.model.user

import org.chewing.v1.model.emoticon.Emoticon

class UserStatus private constructor(
    val statusId: String,
    val statusMessage: String,
    val emoticon: Emoticon,
    val userId: String
) {
    companion object {
        fun of(statusId: String, statusMessage: String, emoticon: Emoticon, userId: String): UserStatus {
            return UserStatus(statusId, statusMessage, emoticon, userId)
        }
    }
}