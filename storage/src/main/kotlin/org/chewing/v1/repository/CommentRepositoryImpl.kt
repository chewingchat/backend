package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jparepository.FeedCommentJpaRepository
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.comment.Comment
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl(
    private val commentJpaRepository: FeedCommentJpaRepository
) : CommentRepository {
    override fun isCommentsOwner(userId: User.UserId, commentIds: List<Comment.CommentId>): Boolean {
        return commentJpaRepository.existsAllByFeedCommentIdInAndUserId(commentIds.map { it.value() }, userId.value())
    }

    override fun readCommentsWithUserId(feedId: Feed.FeedId): List<Pair<User.UserId,Comment>> {
        return commentJpaRepository.findAllByFeedId(feedId.value()).map {
            Pair(User.UserId.of(it.userId), it.toComment())
        }
    }

    override fun removeComment(commentId: Comment.CommentId) {
        commentJpaRepository.deleteById(commentId.value())
    }

    override fun appendComment(user: User, comment: String, feed: Feed) {
        commentJpaRepository.save(FeedCommentJpaEntity.generate(comment, user, feed))
    }

    override fun readCommentsWithFeedId(userId: User.UserId): List<Pair<Feed.FeedId, Comment>> {
        return commentJpaRepository.findAllByUserId(userId.value()).map {
            Pair(Feed.FeedId.of(it.feedId), it.toComment())
        }
    }
}