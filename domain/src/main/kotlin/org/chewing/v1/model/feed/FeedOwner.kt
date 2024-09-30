package org.chewing.v1.model.feed

enum class FeedOwner {
    OWNED, FRIEND, HIDDEN;

    companion object {
        fun target(userId: String, targetUserId: String): FeedOwner {
            return when (userId) {
                targetUserId -> OWNED
                else -> FRIEND
            }
        }
    }
}