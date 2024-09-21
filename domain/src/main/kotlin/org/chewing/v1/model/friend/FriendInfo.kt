package org.chewing.v1.model.friend

import org.chewing.v1.model.UserName

class FriendInfo private constructor(
    val friendId: String,
    val friendName: UserName,
    val isFavorite: Boolean,
) {
    companion object {
        fun of(friendId: String, friendName: UserName, isFavorite: Boolean): FriendInfo {
            return FriendInfo(
                friendId = friendId,
                friendName = friendName,
                isFavorite = isFavorite,
            )
        }
    }
}