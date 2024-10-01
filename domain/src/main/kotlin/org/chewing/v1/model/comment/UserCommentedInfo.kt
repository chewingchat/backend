package org.chewing.v1.model.comment

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.Friend

class UserCommentedInfo private constructor(
    val comment: CommentInfo,
    val friend: Friend,
    val feed: Feed
){
    companion object {
        fun of(comment: CommentInfo, friend: Friend, feed: Feed): UserCommentedInfo {
            return UserCommentedInfo(
                comment = comment,
                friend = friend,
                feed = feed
            )
        }
    }
}