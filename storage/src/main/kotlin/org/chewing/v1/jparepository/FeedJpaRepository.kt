package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface FeedJpaRepository : JpaRepository<FeedJpaEntity, String> {
    fun existsAllByFeedIdInAndUserId(feedId: List<String>, writerId: String): Boolean
    fun existsByFeedIdAndUserId(feedId: String, writerId: String): Boolean
    fun findAllByUserIdAndHideTrueOrderByCreatedAtAsc(userId: String): List<FeedJpaEntity>
    fun findAllByUserIdAndHideFalseOrderByCreatedAtAsc(userId: String): List<FeedJpaEntity>
    fun findAllByUserIdOrderByCreatedAtAsc(userId: String): List<FeedJpaEntity>
    fun deleteAllByUserId(userId: String)
    fun findAllByFeedIdInOrderByCreatedAtAsc(feedIds: List<String>): List<FeedJpaEntity>}
