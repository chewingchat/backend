package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.Friend
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

    @Version
    @Column(name = "version")
    var version: Long = 0,

    @JoinColumn(name = "feed_id")
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val feedDetails: List<FeedDetailJpaEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    val writer: UserJpaEntity,
) : BaseEntity() {
    companion object {
        fun fromFeed(feed: Feed): FeedJpaEntity {
            return FeedJpaEntity(
                feedId = feed.feedId.value(),
                feedTopic = feed.feedTopic,
                likes = feed.likes,
                feedDetails = feed.feedDetails.map { FeedDetailJpaEntity.fromFeedDetail(it) },
                writer = UserJpaEntity.fromUser(feed.writer)
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
            writer = User.empty()
        )
    }

    fun toFeedWithDetails(): Feed {
        return Feed.of(
            feedId = feedId,
            feedTopic = feedTopic,
            likes = likes,
            feedUploadTime = createdAt!!,
            feedDetails = feedDetails.map { it.toFeedDetail() },
            writer = writer.toUser()
        )
    }
}