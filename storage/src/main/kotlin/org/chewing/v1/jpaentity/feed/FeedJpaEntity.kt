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
internal class FeedJpaEntity(
    @Id
    private val feedId: String = UUID.randomUUID().toString(),
    private val feedTopic: String,
    private var likes: Int,
    private var comments: Int,
    @Version
    private var version: Long? = 0,
    private val userId: String,
    private var hide: Boolean,
) : BaseEntity() {
    companion object {
        fun generate(
            topic: String,
            userId: String,
        ): FeedJpaEntity {
            return FeedJpaEntity(
                feedTopic = topic,
                likes = 0,
                comments = 0,
                userId = userId,
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

    fun updateHide() {
        this.hide = true
    }

    fun updateUnHide() {
        this.hide = false
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
        )
    }

    fun toUserId(): String {
        return userId
    }
}
