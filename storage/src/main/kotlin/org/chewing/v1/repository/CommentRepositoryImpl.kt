package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jparepository.FeedCommentJpaRepository
import org.chewing.v1.model.user.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.comment.CommentInfo
import org.springframework.stereotype.Repository

@Repository
internal class CommentRepositoryImpl(
    private val commentJpaRepository: FeedCommentJpaRepository
) : CommentRepository {

    override fun reads(feedId: String): List<CommentInfo> {
        return commentJpaRepository.findAllByFeedId(feedId).map {
            it.toCommentInfo()
        }
    }

    override fun readsIn(commentIds: List<String>): List<CommentInfo> {
        return commentJpaRepository.findAllByFeedCommentIdIn(commentIds).map {
            it.toCommentInfo()
        }
    }

    override fun remove(commentId: String) {
        commentJpaRepository.deleteById(commentId)
    }

    override fun removes(feedIds: List<String>) {
        commentJpaRepository.deleteAllByFeedIdIn(feedIds)
    }

    override fun append(userId: String, feedId: String, comment: String) {
        commentJpaRepository.save(FeedCommentJpaEntity.generate(userId, feedId, comment))
    }

    override fun readsCommented(userId: String): List<CommentInfo> {
        return commentJpaRepository.findAllByUserId(userId).map {
            it.toCommentInfo()
        }
    }
}