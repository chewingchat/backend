package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface FeedJpaRepository : JpaRepository<FeedJpaEntity, String> {
    fun findAllByWriterId(writerId: String): List<FeedJpaEntity>
}