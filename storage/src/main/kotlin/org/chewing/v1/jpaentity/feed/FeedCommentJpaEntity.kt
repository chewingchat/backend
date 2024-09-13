package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.media.Video
import org.hibernate.annotations.DynamicInsert
import java.util.*

@Entity
@DynamicInsert
@Table(name = "feed_comment", schema = "chewing")
class FeedCommentJpaEntity(
    @Id
    @Column(name = "feed_comment_id")
    val feedCommentId: String = UUID.randomUUID().toString(),

    @Column(name = "feed_comment", nullable = false)
    val comment: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val writer: UserJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feed_id", insertable = false, updatable = false)
    val feed: FeedJpaEntity,
) : BaseEntity() {
    companion object {
        fun fromFeedComment(comment: FeedComment, feed: Feed): FeedCommentJpaEntity {
            return FeedCommentJpaEntity(
                comment = comment.comment,
                writer = UserJpaEntity.fromUser(comment.writer),
                feed = FeedJpaEntity.fromFeed(feed)
            )
        }
    }

    fun toFeedComment(): FeedComment {
        return FeedComment(
            commentId = FeedComment.CommentId.of(feedCommentId),
            comment = comment,
            writer = writer.toUser(),
            createAt = createdAt!!
        )
    }

    fun toFeed(): Feed {
        return feed.toFeed()
    }

    fun toFeedWithDetails(): Feed{
        return feed.toFeedWithDetails()
    }
}
