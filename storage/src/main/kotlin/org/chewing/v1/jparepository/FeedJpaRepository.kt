package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedJpaRepository : JpaRepository<FeedJpaEntity, String> {
    @Query("SELECT f FROM FeedJpaEntity f LEFT JOIN FETCH f.feedDetails LEFT JOIN FETCH f.writer WHERE f.feedId = :feedId")
    fun findByIdWithDetails(
        @Param("feedId") feedId: String
    ): Optional<FeedJpaEntity>
    @Query("SELECT f FROM FeedJpaEntity f LEFT JOIN FETCH f.feedDetails LEFT JOIN FETCH f.writer WHERE f.writer.id = :writerId")
    fun findByWriterIdWithDetails(
        @Param("writerId") writerId: String
    ): List<FeedJpaEntity>
}