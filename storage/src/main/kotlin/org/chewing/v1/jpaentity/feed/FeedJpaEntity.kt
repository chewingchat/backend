package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "feed", schema = "chewing")
class FeedJpaEntity(
    @Id
    val feedId: String = UUID.randomUUID().toString(),
    val feedTopic: String,
    private val likes: Int,
    private val comments: Int,

    @Version
    var version: Long? = 0,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    var feedDetails: MutableList<FeedDetailJpaEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val writer: UserJpaEntity
) : BaseEntity() {
    companion object {
        fun fromFeed(feed: Feed): FeedJpaEntity {
            return FeedJpaEntity(
                feedId = feed.id.value(),
                feedTopic = feed.topic,
                likes = feed.likes,
                feedDetails = feed.details.map { FeedDetailJpaEntity.fromFeedDetail(it) }.toMutableList(),
                writer = UserJpaEntity.fromUser(feed.writer),
                version = feed.version,
                comments = feed.comments
            )
        }
    }

    fun toFeed(): Feed {
        return Feed.of(
            id = feedId,
            topic = feedTopic,
            likes = likes,
            uploadAt = createdAt!!,
            details = emptyList(),
            writer = User.empty(),
            version = version!!,
            comments = comments
        )
    }

    fun toFeedWithDetails(): Feed {
        return Feed.of(
            id = feedId,
            topic = feedTopic,
            likes = likes,
            uploadAt = createdAt!!,
            details = feedDetails.map { it.toFeedDetail() },
            writer = writer.toUser(),
            version = version!!,
            comments = comments
        )
    }

    fun toFeedWithWriter(): Feed {
        return Feed.of(
            id = feedId,
            topic = feedTopic,
            likes = likes,
            uploadAt = createdAt!!,
            details = emptyList(),
            writer = writer.toUser(),
            version = version!!,
            comments = comments
        )
    }
}
