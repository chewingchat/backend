package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.comment.Comment
import org.hibernate.annotations.DynamicInsert
import java.util.*

@Entity
@DynamicInsert
@Table(name = "feed_comment", schema = "chewing")
class FeedCommentJpaEntity(
    @Id
    val feedCommentId: String = UUID.randomUUID().toString(),
    val comment: String,
    val userId: String,
    val feedId: String
) : BaseEntity() {
    companion object {
        fun generate(comment: String, writer: User, feed: Feed): FeedCommentJpaEntity {
            return FeedCommentJpaEntity(
                comment = comment,
                userId = writer.userId.value(),
                feedId = feed.id.value()
            )
        }
    }
    fun toComment(): Comment {
        return Comment(
            Comment.CommentId.of(feedCommentId),
            comment,
            this.createdAt!!
        )
    }
}
