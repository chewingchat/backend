package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jparepository.FeedCommentJpaRepository
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.comment.CommentInfo
import org.springframework.stereotype.Repository

@Repository
internal class CommentRepositoryImpl(
    private val commentJpaRepository: FeedCommentJpaRepository
) : CommentRepository {
    override fun isCommentsOwner(userId: String, commentIds: List<String>): Boolean {
        return commentJpaRepository.existsAllByFeedCommentIdInAndUserId(commentIds, userId)
    }

    override fun readComment(feedId: String): List<CommentInfo> {
        return commentJpaRepository.findAllByFeedId(feedId).map {
            it.toCommentInfo()
        }
    }

    override fun removeComment(commentId: String) {
        commentJpaRepository.deleteById(commentId)
    }

    override fun removeCommented(userId: String) {
        commentJpaRepository.deleteAllByUserId(userId)
    }

    override fun removesByFeedId(feedIds: List<String>) {
        commentJpaRepository.deleteAllByFeedIdIn(feedIds)
    }

    override fun appendComment(user: User, comment: String, feedInfo: FeedInfo) {
        commentJpaRepository.save(FeedCommentJpaEntity.generate(comment, user, feedInfo))
    }

    override fun readCommented(userId: String): List<CommentInfo> {
        return commentJpaRepository.findAllByUserId(userId).map {
            it.toCommentInfo()
        }
    }

    override fun read(commentId: String): CommentInfo? {
        return commentJpaRepository.findById(commentId).map {
            it.toCommentInfo()
        }.orElse(null)
    }
}