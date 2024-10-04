package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.user.User
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@DynamicInsert
@Table(name = "feed_likes", schema = "chewing")
internal class UserFeedLikesJpaEntity(
    @EmbeddedId
    private val userFeedId: UserFeedId,
    private val likeTime: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromUserFeed(userId: String, feedId: String): UserFeedLikesJpaEntity {
            return UserFeedLikesJpaEntity(
                userFeedId = UserFeedId(userId = userId, feedId = feedId),
            )
        }
    }
}