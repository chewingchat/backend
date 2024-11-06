package org.chewing.v1.repository.user

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jparepository.feed.FeedCommentJpaRepository
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.repository.feed.CommentRepository
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

    override fun remove(commentId: String): String? {
        return commentJpaRepository.findById(commentId).map {
            commentJpaRepository.deleteById(commentId)
            it.toCommentInfo().feedId
        }.orElse(null)
    }

    override fun removes(feedIds: List<String>) {
        commentJpaRepository.deleteAllByFeedIdIn(feedIds)
    }

    override fun append(userId: String, feedId: String, comment: String) {
        commentJpaRepository.save(FeedCommentJpaEntity.generate(userId, feedId, comment))
    }

    override fun readsOwned(userId: String): List<CommentInfo> {
        return commentJpaRepository.findAllByUserId(userId).map {
            it.toCommentInfo()
        }
    }
}
