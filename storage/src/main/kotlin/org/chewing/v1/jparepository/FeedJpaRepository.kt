package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface FeedJpaRepository : JpaRepository<FeedJpaEntity, String> {
    fun findByUserId(string: String): List<FeedJpaEntity>
    fun existsByFeedIdAndUserId(feedId: String, writerId: String): Boolean
    fun existsAllByFeedIdInAndUserId(feedId: List<String>, writerId: String): Boolean
    fun findAllByFeedIdInAndUserIdIn(feedId: List<String>, writerId: List<String>): List<FeedJpaEntity>
    fun deleteAllByUserId(userId: String)
    fun findAllByUserId(userId: String): List<FeedJpaEntity>
    fun findAllByUserIdAndHideFalse(userId: String): List<FeedJpaEntity>
    fun findAllByUserIdAndHideTrue(userId: String): List<FeedJpaEntity>
}
