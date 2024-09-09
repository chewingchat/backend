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
    @Column(name = "feed_id")
    val feedId: String = UUID.randomUUID().toString(),

    @Column(name = "feed_topic", nullable = false)
    val feedTopic: String,

    @Column(name = "likes", nullable = false)
    private val likes: Int,

    @Column(name = "comments", nullable = false)
    private val comments: Int,

    @Version
    @Column(name = "version")
    var version: Long? = 0,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", insertable = false, updatable = false)
    var feedDetails: MutableList<FeedDetailJpaEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val writer: UserJpaEntity
) : BaseEntity() {
    companion object {
        fun fromFeed(feed: Feed): FeedJpaEntity {
            return FeedJpaEntity(
                feedId = feed.feedId.value(),
                feedTopic = feed.feedTopic,
                likes = feed.likes,
                feedDetails = feed.feedDetails.map { FeedDetailJpaEntity.fromFeedDetail(it) }.toMutableList(),
                writer = UserJpaEntity.fromUser(feed.writer),
                version = feed.version,
                comments = feed.comments
            )
        }
    }

    fun toFeed(): Feed {
        return Feed.of(
            feedId = feedId,
            feedTopic = feedTopic,
            likes = likes,
            feedUploadTime = createdAt!!,
            feedDetails = emptyList(),
            writer = User.empty(),
            version = version!!,
            comments = comments
        )
    }

    fun toFeedWithDetails(): Feed {
        return Feed.of(
            feedId = feedId,
            feedTopic = feedTopic,
            likes = likes,
            feedUploadTime = createdAt!!,
            feedDetails = feedDetails.map { it.toFeedDetail() },
            writer = writer.toUser(),
            version = version!!,
            comments = comments
        )
    }

    fun toFeedWithWriter(): Feed {
        return Feed.of(
            feedId = feedId,
            feedTopic = feedTopic,
            likes = likes,
            feedUploadTime = createdAt!!,
            feedDetails = emptyList(),
            writer = writer.toUser(),
            version = version!!,
            comments = comments
        )
    }
}
