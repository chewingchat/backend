package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface FeedCommentJpaRepository : JpaRepository<FeedCommentJpaEntity, String> {
    @Query("SELECT f FROM FeedCommentJpaEntity f JOIN FETCH f.writer WHERE f.feed.feedId = :feedId ORDER BY f.createdAt")
    fun findAllByFeedIdWithWriter(feedId: String): List<FeedCommentJpaEntity>

    @Query("SELECT f FROM FeedCommentJpaEntity f JOIN FETCH f.writer WHERE f.feedCommentId IN :commentIds ORDER BY f.createdAt")
    fun findAllByIdsWithWriter(@Param("commentIds") commentIds: List<String>): List<FeedCommentJpaEntity>

    fun deleteAllByFeedCommentIdIn(commentIds: List<String>)
}