package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.TestDataFactory
import org.chewing.v1.TestDataFactory.createUser
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.service.CommentService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.format.DateTimeFormatter

@WebMvcTest(CommentController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class CommentControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var commentService: CommentService

    private fun performCommonSuccessCreateResponse(result: ResultActions) {
        result.andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("생성 완료"))
    }

    private fun performCommonSuccessResponse(result: ResultActions) {
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("성공"))
    }


    @Test
    @DisplayName("피드 댓글 추가")
    fun addFeedComment() {
        // Given
        val requestBody = mapOf(
            "feedId" to "feedId",
            "comment" to "comment",
        )
        // When
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/feed/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "userId")
                .content(objectMapper.writeValueAsString(requestBody))
        )
        // Then
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("피드 댓글 삭제")
    fun deleteFeedComment() {
        // Given
        val requestBody = listOf(
            mapOf("commentId" to "commentId1"),
            mapOf("commentId" to "commentId2"),
        )
        // When
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/feed/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "userId")
                .content(objectMapper.writeValueAsString(requestBody))
        )
        // Then
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("내 피드 댓글 조회")
    fun getFeedComments() {
        // Given
        val feedId = "feedId"

        val comment = TestDataFactory.createComment()
        val formattedCommentTime = comment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
        // When
        whenever(commentService.fetchComment("userId", feedId)).thenReturn(
            listOf(comment, comment))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/$feedId/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "userId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].friend.friendId").value(comment.writer.userId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].friend.firstName").value(comment.writer.name.firstName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].friend.lastName").value(comment.writer.name.lastName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].friend.imageUrl").value(comment.writer.image.url))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].friend.access").value(comment.writer.type.name.lowercase()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].comment.commentId").value(comment.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].comment.comment").value(comment.comment))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0].comment.commentTime").value(formattedCommentTime))
    }

}