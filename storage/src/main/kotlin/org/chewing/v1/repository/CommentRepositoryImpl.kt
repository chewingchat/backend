package org.chewing.v1.repository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jparepository.FeedCommentJpaRepository
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl(
    private val commentJpaRepository: FeedCommentJpaRepository
) : CommentRepository {
    override fun readComments(commentIds: List<FeedComment.CommentId>): List<FeedComment> {
        return commentJpaRepository.findAllByIdsWithWriter(commentIds.map { it.value() }).map { it.toFeedComment() }
    }

    override fun removeComment(commentId: FeedComment.CommentId) {
        commentJpaRepository.deleteById(commentId.value())
    }

    override fun appendComment(user: User, comment: String, feed: Feed) {
        commentJpaRepository.save(FeedCommentJpaEntity.generate(comment, user, feed))
    }

    override fun readUserCommentsFulledFeeds(userId: User.UserId): List<Pair<FeedComment, Feed>> {
        return commentJpaRepository.findAllByUserIdWithWriter(userId.value())
            .map { Pair(it.toFeedComment(), it.toFeed()) }
    }

    override fun readFeedComments(feedId: Feed.FeedId): List<FeedComment> {
        return commentJpaRepository.findAllByFeedId(feedId.value()).map { it.toFeedComment() }
    }
}