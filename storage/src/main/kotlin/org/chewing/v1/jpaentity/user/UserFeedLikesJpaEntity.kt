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
    val likeTime: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromUserFeed(user: User, feed: Feed): UserFeedLikesJpaEntity {
            return UserFeedLikesJpaEntity(
                id = UserFeedId(userId = user.userId.value(), feedId = feed.id.value()),
            )
        }
    }
}