package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.RestDocsTest
import org.chewing.v1.controller.feed.FeedLikesController
import org.chewing.v1.service.feed.FeedLikesService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ActiveProfiles("test")
class FeedLikesControllerTest : RestDocsTest() {

    private lateinit var feedLikesService: FeedLikesService
    private lateinit var objectMapper: ObjectMapper
    private lateinit var feedLikesController: FeedLikesController

    @BeforeEach
    fun setUp() {
        feedLikesService = mock()
        feedLikesController = FeedLikesController(feedLikesService)
        mockMvc = mockController(feedLikesController)
        objectMapper = objectMapper()
    }

    @Test
    @DisplayName("피드 좋아요 추가")
    fun `addFeedLikes`() {
        val userId = "testUserId"
        val requestBody = mapOf(
            "feedId" to "testFeedId",
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/feed/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody)),
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("피드 좋아요 취소")
    fun `deleteFeedLikes`() {
        val userId = "testUserId"
        val requestBody = mapOf(
            "feedId" to "testFeedId",
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/feed/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody)),
        )
        performCommonSuccessResponse(result)
    }
}
