package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.RestDocsTest
import org.chewing.v1.controller.user.UserController
import org.chewing.v1.facade.AccountFacade
import org.chewing.v1.service.user.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ActiveProfiles("test")
class UserControllerTest : RestDocsTest() {

    private lateinit var userService: UserService
    private lateinit var accountFacade: AccountFacade
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        userService = mock()
        accountFacade = mock()
        val userController = UserController(userService, accountFacade)
        mockMvc = mockController(userController)
        objectMapper = objectMapper()
    }

    @Test
    @DisplayName("프로필 이미지 변경")
    fun changeProfileImage() {
        val mockFile = MockMultipartFile(
            "file",
            "testFile.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray(),
        )

        // When: 파일 업로드 요청을 보냄
        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/user/image")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .requestAttr("userId", "testUserId") // userId 전달
                .param("category", "PROFILE"),
        )
        // Then: 응답 코드와 메시지 검증
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 엑세싱")
    fun makeAccess() {
        // Given: 사용자 엑세스 요청
        val requestBody = mapOf(
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "birth" to "2021-01-01",
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/user/access")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 이름 변경")
    fun updateName() {
        val requestBody = mapOf(
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 삭제")
    fun deleteUser() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user")
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        verify(accountFacade).deleteAccount(any())
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 생일 변경")
    fun changeBirth() {
        val requestBody = mapOf(
            "birth" to "2021-01-01",
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/birth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId"), // userId 전달
        )
        performCommonSuccessResponse(result)
    }
}
