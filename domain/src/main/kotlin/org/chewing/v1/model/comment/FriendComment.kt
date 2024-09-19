package org.chewing.v1.model.comment

import org.chewing.v1.model.friend.Friend

class FriendComment(
    val comment: Comment,
    val friend: Friend
) {
    companion object {
        fun of(
            comment: Comment,
            friend: Friend
        ): FriendComment {
            return FriendComment(
                comment = comment,
                friend = friend
            )
        }
    }
}