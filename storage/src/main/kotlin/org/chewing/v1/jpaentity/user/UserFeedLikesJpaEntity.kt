package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@DynamicInsert
@Table(name = "feed_likes", schema = "chewing")
internal class UserFeedLikesJpaEntity(
    @EmbeddedId
    val userFeedId: UserFeedId,
    val likeTime: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromUserFeed(user: User, feedInfo: FeedInfo): UserFeedLikesJpaEntity {
            return UserFeedLikesJpaEntity(
                userFeedId = UserFeedId(userId = user.userId, feedId = feedInfo.feedId),
            )
        }
    }
}