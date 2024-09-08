package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Component

@Component
object FeedValidator {
    fun isFeedOwner(feed: Feed, user: User) {
        if (feed.writer.userId != user.userId) {
            throw ConflictException(ErrorCode.FEED_IS_NOT_OWNED)
        }
    }
    fun isCommentOwner(comment: List<FeedComment>, user: User) {
        if (comment.any { it.writer.userId != user.userId }) {
            throw ConflictException(ErrorCode.COMMENT_IS_NOT_OWNED)
        }
    }
}