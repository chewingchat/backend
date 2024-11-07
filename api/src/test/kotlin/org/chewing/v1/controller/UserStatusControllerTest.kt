package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.RestDocsTest
import org.chewing.v1.TestDataFactory
import org.chewing.v1.controller.user.UserStatusController
import org.chewing.v1.service.user.UserStatusService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ActiveProfiles("test")
class UserStatusControllerTest : RestDocsTest() {

    private lateinit var userStatusService: UserStatusService
    private lateinit var objectMapper: ObjectMapper
    private lateinit var userStatusController: UserStatusController

    @BeforeEach
    fun setUp() {
        userStatusService = mock()
        userStatusController = UserStatusController(userStatusService)
        mockMvc = mockController(userStatusController)
        objectMapper = objectMapper()
    }

    @Test
    @DisplayName("사용자 상태 선택 해제")
    fun deleteProfileSelectedStatus() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/status/select")
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 상태 삭제")
    fun deleteProfileStatus() {
        val requestBody = listOf(
            mapOf("statusId" to 1),
            mapOf("statusId" to 2),
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 상태 선택")
    fun changeProfileSelectedStatus() {
        val requestBody = mapOf(
            "statusId" to 1,
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/status/select")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 상태 추가")
    fun addProfileStatus() {
        val requestBody = mapOf(
            "message" to "testMessage",
            "emoji" to "testEmoji",
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/user/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("사용자 상태 조회")
    fun getUserStatus() {
        val statuses = listOf(TestDataFactory.createUserStatus())
        whenever(userStatusService.getUserStatuses(any())).thenReturn(statuses)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/user/status")
                .requestAttr("userId", "testUserId"), // userId 전달
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.statuses[0].statusId").value(statuses[0].statusId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.statuses[0].message").value(statuses[0].message))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.statuses[0].emoji").value(statuses[0].emoji))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.statuses[0].selected").value(statuses[0].isSelected))
    }
}
