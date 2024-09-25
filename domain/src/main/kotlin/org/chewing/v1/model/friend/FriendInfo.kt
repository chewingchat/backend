package org.chewing.v1.model.friend

import org.chewing.v1.model.ActivateType
import org.chewing.v1.model.UserName

class FriendInfo private constructor(
    val friendId: String,
    val friendName: UserName,
    val isFavorite: Boolean,
    val type: ActivateType
) {
    companion object {
        fun of(friendId: String, friendName: UserName, isFavorite: Boolean, type: ActivateType): FriendInfo {
            return FriendInfo(
                friendId = friendId,
                friendName = friendName,
                isFavorite = isFavorite,
                type
            )
        }
    }
}