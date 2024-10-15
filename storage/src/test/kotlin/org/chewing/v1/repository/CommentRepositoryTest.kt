package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.feed.FeedCommentJpaRepository
import org.chewing.v1.repository.support.TestDataGenerator
import org.chewing.v1.repository.user.CommentRepositoryImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class CommentRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var commentJpaRepository: FeedCommentJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val commentRepositoryImpl: CommentRepositoryImpl by lazy {
        CommentRepositoryImpl(commentJpaRepository)
    }

    @Test
    fun `댓글을 추가해야 한다`() {
        val userId = "userId"
        val feedId = "feedId"
        val comment = "comment"
        commentRepositoryImpl.append(userId, feedId, comment)
        val result = commentJpaRepository.findAllByUserId(userId)
        assert(result.size == 1)
    }

    @Test
    fun `댓글을 조회해야 한다`() {
        val userId = "userId2"
        val feedId = "feedId2"
        val comments = testDataGenerator.feedCommentEntityDataList(userId, feedId)
        val result = commentRepositoryImpl.reads(feedId)
        assert(result.size == comments.size)
    }

    @Test
    fun `댓글을 삭제해야 한다`() {
        val userId = "userId3"
        val feedId = "feedId3"
        val comment = testDataGenerator.feedCommentEntityData(userId, feedId)
        val result = commentRepositoryImpl.remove(comment.commentId)
        assert(result != null)
    }

    @Test
    fun `댓글 삭제 실패 댓글이 없어야 한다`() {
        val commentId = "commentId"
        val result = commentRepositoryImpl.remove(commentId)
        assert(result == null)
    }

    @Test
    fun `댓글아이디를 가지고 모든 댓글을 조회해야 한다`() {
        val userId = "userId4"
        val feedId = "feedId5"
        val comments = testDataGenerator.feedCommentEntityDataList(userId, feedId)
        val result = commentRepositoryImpl.readsIn(comments.map { it.commentId })
        assert(result.size == comments.size)
    }

    @Test
    fun `모든 댓글을 삭제해야 한다`() {
        val userId = "userId5"
        val feedId = "feedId6"
        testDataGenerator.feedCommentEntityDataList(userId, feedId)
        commentRepositoryImpl.removes(listOf(feedId))
        val result = commentJpaRepository.findAllByFeedId(feedId)
        assert(result.isEmpty())
    }

    @Test
    fun `댓글을 작성한 사용자의 댓글을 조회해야 한다`() {
        val userId = "userId6"
        val userId2 = "userId7"
        val feedId = "feedId7"
        val comments = testDataGenerator.feedCommentEntityDataList(userId, feedId)
        val comments2 = testDataGenerator.feedCommentEntityDataList(userId2, feedId)
        val result = commentRepositoryImpl.readsOwned(userId)
        val result2 = commentRepositoryImpl.readsOwned(userId2)
        assert(result.size == comments.size)
        assert(result2.size == comments2.size)
    }
}