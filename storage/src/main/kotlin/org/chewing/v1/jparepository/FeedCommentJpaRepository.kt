package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface FeedCommentJpaRepository : JpaRepository<FeedCommentJpaEntity, String> {
    fun findAllByUserId(userId: String): List<FeedCommentJpaEntity>
    fun findAllByFeedId(feedId: String): List<FeedCommentJpaEntity>
    fun existsAllByFeedCommentIdInAndUserId(feedCommentIds: List<String>, userId: String): Boolean
    fun deleteAllByUserId(userId: String)
    fun deleteAllByFeedIdIn(feedIds: List<String>)
}