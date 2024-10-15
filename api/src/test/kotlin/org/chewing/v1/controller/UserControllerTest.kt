package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.user.UserController
import org.chewing.v1.facade.AccountFacade
import org.chewing.v1.service.user.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
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

@WebMvcTest(UserController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class UserControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var accountFacade: AccountFacade

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
    @DisplayName("프로필 이미지 변경")
    fun changeProfileImage() {
        val mockFile = MockMultipartFile(
            "file",
            "testFile.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray()
        )

        // When: 파일 업로드 요청을 보냄
        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/user/image")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .requestAttr("userId", "testUserId")  // userId 전달
                .param("category", "profile")
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
            "birth" to "2021-01-01"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/user/access")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")  // userId 전달
        )
        performCommonSuccessResponse(result)
    }


    @Test
    @DisplayName("사용자 이름 변경")
    fun updateName() {
        val requestBody = mapOf(
            "firstName" to "testFirstName",
            "lastName" to "testLastName"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")  // userId 전달
        )
        performCommonSuccessResponse(result)
    }

    @Test
    @DisplayName("사용자 삭제")
    fun deleteUser() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user")
                .requestAttr("userId", "testUserId")  // userId 전달
        )
        verify(accountFacade).deleteAccount(any())
        performCommonSuccessResponse(result)
    }


    @Test
    @DisplayName("사용자 생일 변경")
    fun changeBirth() {
        val requestBody = mapOf(
            "birth" to "2021-01-01"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/birth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")  // userId 전달
        )
        performCommonSuccessResponse(result)
    }
}

