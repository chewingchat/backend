package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FeedCommentJpaRepository : JpaRepository<FeedCommentJpaEntity, String> {
    @Query("SELECT f FROM FeedCommentJpaEntity f JOIN FETCH f.feed WHERE f.feedCommentId IN :commentIds ORDER BY f.createdAt")
    fun findAllByIdsWithWriter(@Param("commentIds") commentIds: List<String>): List<FeedCommentJpaEntity>
    @Query("SELECT f FROM FeedCommentJpaEntity f JOIN FETCH f.writer Join Fetch f.feed WHERE f.feedCommentId = :commentId ")
    fun findByIdWithFeedAndWriter(@Param("commentId") commentId: String): FeedCommentJpaEntity

    @Query("""
    SELECT f 
    FROM FeedCommentJpaEntity f
    JOIN FETCH f.feed feed
    JOIN FETCH feed.writer writer
    JOIN FETCH feed.feedDetails details
    WHERE f.writer.id = :userId
    """)
    fun findAllByUserIdWithFeed(@Param("userId") userId: String): List<FeedCommentJpaEntity>
    @Query("SELECT f FROM FeedCommentJpaEntity f JOIN FETCH f.writer WHERE f.feed.feedId = :feedId")
    fun findAllByFeedId(feedId: String): List<FeedCommentJpaEntity>
}