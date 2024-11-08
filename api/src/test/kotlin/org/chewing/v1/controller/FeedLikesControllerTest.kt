package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.feed.FeedLikesController
import org.chewing.v1.service.feed.FeedLikesService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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

@WebMvcTest(FeedLikesController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class FeedLikesControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var feedLikesService: FeedLikesService

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
    @DisplayName("피드 좋아요 추가")
    fun `addFeedLikes`() {
        val userId = "testUserId"
        val requestBody = mapOf(
            "feedId" to "testFeedId"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/feed/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("피드 좋아요 취소")
    fun `deleteFeedLikes`() {
        val userId = "testUserId"
        val requestBody = mapOf(
            "feedId" to "testFeedId"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/feed/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
    }
}