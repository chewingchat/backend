package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "feed", schema = "chewing")
class FeedJpaEntity(
    @Id
    private val feedId: String = UUID.randomUUID().toString(),
    private val feedTopic: String,
    private var likes: Int,
    private var comments: Int,
    @Version
    var version: Long? = 0,
    val userId: String
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
                userId = writer.userId.value(),
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

    fun toFeedId(): Feed.FeedId {
        return Feed.FeedId.of(feedId)
    }

    fun toFeed(): Feed {
        return Feed.of(
            id = feedId,
            topic = feedTopic,
            likes = likes,
            uploadAt = createdAt!!,
            comments = comments
        )
    }
    fun toUserId(): User.UserId {
        return User.UserId.of(userId)
    }
}
