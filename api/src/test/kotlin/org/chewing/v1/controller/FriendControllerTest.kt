package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.service.FriendService
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

@WebMvcTest(FriendController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class FriendControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var friendService: FriendService

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
    @DisplayName("이메일로 친구 추가")
    fun addFriendWithEmail() {
        val requestBody = mapOf(
            "email" to "test@example.com",
            "firstName" to "testFirstName",
            "lastName" to "testLastName"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/friend/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("전화번호로 친구 추가")
    fun addFriendWithPhone() {
        val requestBody = mapOf(
            "phoneNumber" to "01012345678",
            "countryCode" to "82",
            "firstName" to "testFirstName",
            "lastName" to "testLastName"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/friend/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("즐겨찾기 변경")
    fun changeFavorite() {
        val requestBody = mapOf(
            "friendId" to "testFriendId",
            "favorite" to true
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/friend/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("친구 차단")
    fun blockFriend() {
        val requestBody = mapOf(
            "friendId" to "testFriendId"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/friend/block")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("친구 삭제")
    fun deleteFriend() {
        val requestBody = mapOf(
            "friendId" to "testFriendId"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("친구 이름 변경")
    fun updateFriendName() {
        val requestBody = mapOf(
            "friendId" to "testFriendId",
            "firstName" to "testFirstName",
            "lastName" to "testLastName"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/friend/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")
        )
        performCommonSuccessResponse(result)
    }
}