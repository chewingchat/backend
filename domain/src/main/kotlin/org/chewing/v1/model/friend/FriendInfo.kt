package org.chewing.v1.model.friend

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.user.UserName

class FriendInfo private constructor(
    val friendId: String,
    val friendName: UserName,
    val isFavorite: Boolean,
    val type: AccessStatus
) {
    companion object {
        fun of(friendId: String, friendName: UserName, isFavorite: Boolean, type: AccessStatus): FriendInfo {
            return FriendInfo(
                friendId = friendId,
                friendName = friendName,
                isFavorite = isFavorite,
                type
            )
        }
    }
}