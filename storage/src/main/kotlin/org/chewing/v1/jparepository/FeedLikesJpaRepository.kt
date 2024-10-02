package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.user.UserFeedId
import org.chewing.v1.jpaentity.user.UserFeedLikesJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
internal interface FeedLikesJpaRepository : JpaRepository<UserFeedLikesJpaEntity, UserFeedId> {
    @Query("SELECT u FROM UserFeedLikesJpaEntity u WHERE u.userFeedId.userId = :userId AND u.userFeedId.feedId IN :feedIds")
    fun findAllByUserIdAndFeedIdIn(
        @Param("userId") userId: String,
        @Param("feedIds") feedIds: List<String>
    ): List<UserFeedLikesJpaEntity>
}