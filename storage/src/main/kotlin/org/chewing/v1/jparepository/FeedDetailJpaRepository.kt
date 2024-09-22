package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedDetailJpaEntity
import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface FeedDetailJpaRepository : JpaRepository<FeedDetailJpaEntity, String> {
    fun findAllByFeedId(feedId: String): List<FeedDetailJpaEntity>
    fun findAllByFeedIdIn(feedIds: List<String>): List<FeedDetailJpaEntity>
    fun deleteAllByFeedIdIn(feedIds: List<String>)
}