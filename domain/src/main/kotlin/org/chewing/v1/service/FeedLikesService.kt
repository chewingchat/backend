package org.chewing.v1.service

import org.chewing.v1.implementation.feed.like.FeedLikeChecker
import org.chewing.v1.implementation.feed.like.FeedLikeLocker
import org.chewing.v1.implementation.feed.like.FeedLikeRemover
import org.chewing.v1.implementation.feed.like.FeedLikeValidator
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Component

@Component
class FeedLikesService(
    private val likeValidator: FeedLikeValidator,
    private val likeLocker: FeedLikeLocker,
    private val likeRemover: FeedLikeRemover,
    private val likeChecker: FeedLikeChecker
) {
    fun like(feedId: String, userId: String, target: FeedTarget) {
        likeValidator.isAlreadyLiked(feedId, userId)
        likeLocker.lockFeedLikes(feedId, userId, target)
    }

    fun unlike(feedId: String, userId: String, target: FeedTarget) {
        likeValidator.isAlreadyUnliked(feedId, userId)
        likeLocker.lockFeedUnLikes(feedId, userId, target)
    }

    fun unlikes(feedIds: List<String>) {
        likeRemover.removeAll(feedIds)
    }

    fun checkLike(feedId: String, userId: String): Boolean {
        return likeChecker.checkLike(feedId, userId)
    }
}