package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.media.Media
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
                comments = feed.comments
            )
        }

        fun generate(
            topic: String,
            writer: User,
            medias: List<Media>
        ): FeedJpaEntity {
            return FeedJpaEntity(
                feedTopic = topic,
                likes = 0,
                comments = 0,
                writer = UserJpaEntity.fromUser(writer),
                feedDetails = medias.map {
                    FeedDetailJpaEntity.generate(it)
                }.toMutableList()
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
            details = emptyList(),
            writer = User.empty(),
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
            comments = comments
        )
    }
}
