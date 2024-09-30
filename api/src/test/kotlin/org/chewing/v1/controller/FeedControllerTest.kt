package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.TestDataFactory.createFeed
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.model.feed.FeedOwner
import org.chewing.v1.service.AuthService
import org.chewing.v1.service.FeedService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.format.DateTimeFormatter

@WebMvcTest(FeedController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class FeedControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var feedService: FeedService

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
    @DisplayName("친구 피드 가져오기")
    fun `getFriendFeeds`() {
        val testFriendId = "testFriendId"
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedService.getFeeds(testFriendId, FeedOwner.FRIEND))
            .thenReturn(listOf(feed))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/list/$testFriendId")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feeds[0].feedId").value(feed.feed.feedId))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].mainDetailFileUrl").value(feed.feedDetails[0].media.url)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].type")
                    .value(feed.feedDetails[0].media.type.toString().lowercase())
            )
    }

    @Test
    @DisplayName("내 피드 가져오기")
    fun `getOwnedFeeds`() {
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedService.getFeeds(userId, FeedOwner.OWNED))
            .thenReturn(listOf(feed))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/list")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feeds[0].feedId").value(feed.feed.feedId))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].mainDetailFileUrl").value(feed.feedDetails[0].media.url)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].type")
                    .value(feed.feedDetails[0].media.type.toString().lowercase())
            )
    }

    @Test
    @DisplayName("친구 피드 상세 가져오기")
    fun `getFriendFeed`() {
        val testFeedId = "testFeedId"
        val userId = "testUserId"
        val feed = createFeed()
        val uploadTime = feed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
        whenever(feedService.getFeed(userId, testFeedId, FeedOwner.FRIEND))
            .thenReturn(Pair(feed, true))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/$testFeedId/detail/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feedId").value(feed.feed.feedId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.liked").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.topic").value(feed.feed.topic))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[0].index").value(feed.feedDetails[0].media.index))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[0].fileUrl").value(feed.feedDetails[0].media.url))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.uploadTime").value(uploadTime))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.details[0].type")
                    .value(feed.feedDetails[0].media.type.toString().lowercase())
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].index").value(feed.feedDetails[1].media.index))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].fileUrl").value(feed.feedDetails[1].media.url))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.details[1].type")
                    .value(feed.feedDetails[1].media.type.toString().lowercase())
            )
    }

    @Test
    @DisplayName("내 피드 상세 가져오기")
    fun `getOwnedFeed`() {
        val testFeedId = "testFeedId"
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedService.getFeed(userId, testFeedId, FeedOwner.OWNED))
            .thenReturn(Pair(feed, true))
        val uploadTime = feed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/$testFeedId/detail/owned")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feedId").value(feed.feed.feedId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.liked").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.topic").value(feed.feed.topic))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.likes").value(feed.feed.likes))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments").value(feed.feed.comments))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[0].index").value(feed.feedDetails[0].media.index))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[0].fileUrl").value(feed.feedDetails[0].media.url))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.uploadTime").value(uploadTime))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.details[0].type")
                    .value(feed.feedDetails[0].media.type.toString().lowercase())
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].index").value(feed.feedDetails[1].media.index))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].fileUrl").value(feed.feedDetails[1].media.url))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.details[1].type")
                    .value(feed.feedDetails[1].media.type.toString().lowercase())
            )
    }

    @Test
    @DisplayName("숨김 피드 가져오기")
    fun `getHiddenFeeds`() {
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedService.getFeeds(userId, FeedOwner.HIDDEN))
            .thenReturn(listOf(feed))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/hide")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feeds[0].feedId").value(feed.feed.feedId))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].mainDetailFileUrl").value(feed.feedDetails[0].media.url)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].type")
                    .value(feed.feedDetails[0].media.type.toString().lowercase())
            )
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

    @Test
    @DisplayName("피드 삭제")
    fun `deleteFeeds`() {
        val userId = "testUserId"
        val requestBody = listOf(
            mapOf(
                "feedId" to "testFeedId"
            ),
            mapOf(
                "feedId" to "testFeedId2"
            )
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("피드 추가")
    fun `addFeed`() {
        val mockFile1 = MockMultipartFile(
            "files",
            "0.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray()
        )
        val mockFile2 = MockMultipartFile(
            "files",
            "1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray()
        )

        // When: 파일 업로드 요청을 보냄
        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/feed")
                .file(mockFile1)
                .file(mockFile2)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .requestAttr("userId", "testUserId")  // userId 전달
                .param("topic", "testTopic")

        )
        performCommonSuccessCreateResponse(result)
    }


}