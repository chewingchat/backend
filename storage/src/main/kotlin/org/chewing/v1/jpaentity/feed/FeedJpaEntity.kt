package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.user.User
import org.chewing.v1.model.feed.FeedInfo
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "feed", schema = "chewing",
    indexes = [Index(name = "idx_feed_hide", columnList = "hide")]
)
class FeedJpaEntity(
    @Id
    private val feedId: String = UUID.randomUUID().toString(),
    private val feedTopic: String,
    private var likes: Int,
    private var comments: Int,
    @Version
    var version: Long? = 0,
    val userId: String,
    val hide: Boolean,
) : BaseEntity() {
    companion object {
        fun generate(
            topic: String,
            writer: User,
        ): FeedJpaEntity {
            return FeedJpaEntity(
                feedTopic = topic,
                likes = 0,
                comments = 0,
                userId = writer.userId,
                hide = false
            )
        }
    }

    fun updateLikes() {
        this.likes += 1
    }

    fun updateUnLikes() {
        this.likes -= 1
    }

    fun updateComments() {
        this.comments += 1
    }

    fun updateUnComments() {
        this.comments -= 1
    }

    fun toFeedId(): String {
        return feedId
    }

    fun toFeedInfo(): FeedInfo {
        return FeedInfo.
        of(
            feedId = feedId,
            topic = feedTopic,
            likes = likes,
            comments = comments,
            uploadAt = createdAt!!,
            userId = userId,
            hide = hide
        )
    }

    fun toUserId(): String {
        return userId
    }
}
