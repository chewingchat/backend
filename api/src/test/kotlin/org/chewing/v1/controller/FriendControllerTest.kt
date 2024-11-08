package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.RestDocsTest
import org.chewing.v1.controller.friend.FriendController
import org.chewing.v1.facade.FriendFacade
import org.chewing.v1.service.friend.FriendShipService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ActiveProfiles("test")
class FriendControllerTest : RestDocsTest() {
    private lateinit var friendFacade: FriendFacade
    private lateinit var friendShipService: FriendShipService
    private lateinit var friendController: FriendController
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        friendFacade = mock()
        friendShipService = mock()
        friendController = FriendController(friendFacade, friendShipService)
        mockMvc = mockController(friendController)
        objectMapper = objectMapper()
    }

    @Test
    @DisplayName("이메일로 친구 추가")
    fun addFriendWithEmail() {
        val requestBody = mapOf(
            "email" to "test@example.com",
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/friend/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"),
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
            "lastName" to "testLastName",
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/friend/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"),
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    @DisplayName("즐겨찾기 변경")
    fun changeFavorite() {
        val requestBody = mapOf(
            "friendId" to "testFriendId",
            "favorite" to true,
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/friend/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("친구 차단")
    fun blockFriend() {
        val requestBody = mapOf(
            "friendId" to "testFriendId",
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/friend/block")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("친구 삭제")
    fun deleteFriend() {
        val requestBody = mapOf(
            "friendId" to "testFriendId",
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("친구 이름 변경")
    fun updateFriendName() {
        val requestBody = mapOf(
            "friendId" to "testFriendId",
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/friend/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"),
        )
        performCommonSuccessResponse(result)
    }
}
