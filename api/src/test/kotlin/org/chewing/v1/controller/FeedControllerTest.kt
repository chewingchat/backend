package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.RestDocsTest
import org.chewing.v1.TestDataFactory.createFeed
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.feed.FeedController
import org.chewing.v1.facade.FeedFacade
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.service.feed.FeedService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.format.DateTimeFormatter

@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class FeedControllerTest : RestDocsTest() {
    private lateinit var feedService: FeedService
    private lateinit var feedFacade: FeedFacade
    private lateinit var feedController: FeedController
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        feedFacade = mock()
        feedService = mock()
        feedController = FeedController(feedService, feedFacade)
        mockMvc = mockController(feedController)
        objectMapper = objectMapper()
    }

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
        whenever(feedService.getOwnedFeeds(testFriendId, FeedStatus.NOT_HIDDEN))
            .thenReturn(listOf(feed))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/friend/$testFriendId/feed/list")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feeds[0].feedId").value(feed.feed.feedId))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].mainDetailFileUrl")
                    .value(feed.feedDetails[0].media.url),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].type")
                    .value(feed.feedDetails[0].media.type.value().lowercase()),
            )
    }

    @Test
    @DisplayName("내 피드 가져오기")
    fun `getOwnedFeeds`() {
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedService.getOwnedFeeds(userId, FeedStatus.NOT_HIDDEN))
            .thenReturn(listOf(feed))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/my/feed/list")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feeds[0].feedId").value(feed.feed.feedId))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].mainDetailFileUrl")
                    .value(feed.feedDetails[0].media.url),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].type")
                    .value(feed.feedDetails[0].media.type.value().lowercase()),
            )
    }

    @Test
    @DisplayName("친구 피드 상세 가져오기")
    fun `getFriendFeed`() {
        val testFeedId = "testFeedId"
        val userId = "testUserId"
        val feed = createFeed()
        val uploadTime = feed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
        whenever(feedFacade.getOwnedFeed(userId, testFeedId))
            .thenReturn(Pair(feed, true))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/friend/feed/$testFeedId/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId),
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
                    .value(feed.feedDetails[0].media.type.value().lowercase()),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].index").value(feed.feedDetails[1].media.index))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].fileUrl").value(feed.feedDetails[1].media.url))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.details[1].type")
                    .value(feed.feedDetails[1].media.type.value().lowercase()),
            )
    }

    @Test
    @DisplayName("내 피드 상세 가져오기")
    fun `getOwnedFeed`() {
        val testFeedId = "testFeedId"
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedFacade.getOwnedFeed(userId, testFeedId))
            .thenReturn(Pair(feed, true))
        val uploadTime = feed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/my/feed/$testFeedId/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId),
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
                    .value(feed.feedDetails[0].media.type.value().lowercase()),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].index").value(feed.feedDetails[1].media.index))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.details[1].fileUrl").value(feed.feedDetails[1].media.url))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.details[1].type")
                    .value(feed.feedDetails[1].media.type.value().lowercase()),
            )
    }

    @Test
    @DisplayName("숨긴 피드 가져오기")
    fun `getHiddenFeeds`() {
        val userId = "testUserId"
        val feed = createFeed()
        whenever(feedService.getOwnedFeeds(userId, FeedStatus.HIDDEN))
            .thenReturn(listOf(feed))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/feed/hide")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.feeds[0].feedId").value(feed.feed.feedId))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].mainDetailFileUrl")
                    .value(feed.feedDetails[0].media.url),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.feeds[0].type")
                    .value(feed.feedDetails[0].media.type.value().lowercase()),
            )
    }

    @Test
    @DisplayName("피드 삭제")
    fun `deleteFeeds`() {
        val userId = "testUserId"
        val requestBody = listOf(
            mapOf(
                "feedId" to "testFeedId",
            ),
            mapOf(
                "feedId" to "testFeedId2",
            ),
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody)),
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
            "Test content".toByteArray(),
        )
        val mockFile2 = MockMultipartFile(
            "files",
            "1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray(),
        )

        // When: 파일 업로드 요청을 보냄
        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/feed")
                .file(mockFile1)
                .file(mockFile2)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .requestAttr("userId", "testUserId") // userId 전달
                .param("topic", "testTopic"),

        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("피드 숨김")
    fun `hideFeeds`() {
        val userId = "testUserId"
        val requestBody = listOf(
            mapOf(
                "feedId" to "testFeedId",
            ),
            mapOf(
                "feedId" to "testFeedId2",
            ),
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/feed/hide")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody)),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("피드 숨김 해제")
    fun `unHideFeeds`() {
        val userId = "testUserId"
        val requestBody = listOf(
            mapOf(
                "feedId" to "testFeedId",
            ),
            mapOf(
                "feedId" to "testFeedId2",
            ),
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/feed/hide")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody)),
        )
        performCommonSuccessResponse(result)
    }
}
