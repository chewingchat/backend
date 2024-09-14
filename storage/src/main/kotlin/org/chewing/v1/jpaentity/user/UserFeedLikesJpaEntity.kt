package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@DynamicInsert
@Table(name = "feed_likes", schema = "chewing")
class UserFeedLikesJpaEntity(
    @EmbeddedId
    val id: UserFeedId,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val user: UserJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("feedId")
    @JoinColumn(name = "feed_id")
    val feed: FeedJpaEntity,

    val likeTime: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromUserFeed(user: User, feed: Feed): UserFeedLikesJpaEntity {
            return UserFeedLikesJpaEntity(
                id = UserFeedId(userId = user.userId.value(), feedId = feed.id.value()),
                user = UserJpaEntity.fromUser(user),
                feed = FeedJpaEntity.fromFeed(feed)
            )
        }
    }
}