package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.comment.CommentInfo
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
        fun generate(comment: String, writer: User, feedInfo: FeedInfo): FeedCommentJpaEntity {
            return FeedCommentJpaEntity(
                comment = comment,
                userId = writer.userId,
                feedId = feedInfo.feedId
            )
        }
    }

    fun toCommentInfo(): CommentInfo {
        return CommentInfo.of(
            feedCommentId,
            comment,
            this.createdAt!!,
            userId,
            feedId
        )
    }
}
