package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.model.ActivateType
import org.chewing.v1.model.user.User
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.service.AuthService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
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
import java.time.LocalDateTime

@WebMvcTest(AuthController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class AuthControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var authService: AuthService

    private fun createJwtToken() =
        JwtToken.of("testAccessToken", RefreshToken.of("testRefreshToken", LocalDateTime.now()))

    private fun createUser() = User.of(
        "testUserId",
        "testFirstName",
        "testLastName",
        "2000-00-00",
        Image.of("www.example.com", 0),
        Image.of("www.example.com", 0),
        ActivateType.ACCESS
    )

    private fun performCommonSuccessResponse(result: ResultActions) {
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("성공"))
    }


    @Test
    @DisplayName("휴대폰 인증번호 전송")
    fun sendPhoneVerification() {
        val requestBody = mapOf(
            "countryCode" to "82",
            "phoneNumber" to "010-1234-5678"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/phone/create/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
        verify(authService).sendPhoneVerification(any())
    }

    @Test
    @DisplayName("이메일 인증번호 전송")
    fun sendEmailVerification() {
        val requestBody = mapOf(
            "email" to "test@Example.com"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/email/create/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
        verify(authService).sendEmailVerification(any())
    }

    @Test
    @DisplayName("휴대폰 인증번호 확인")
    fun verifyPhone() {
        val jwtToken = createJwtToken()
        val user = createUser()

        val requestBody = mapOf(
            "countryCode" to "82",
            "phoneNumber" to "010-1234-5678",
            "verificationCode" to "123456",
            "appToken" to "testToken",
            "deviceId" to "testDeviceId",
            "provider" to "testProvider"
        )
        whenever(authService.verifyPhone(any(), any(), any(), any()))
            .thenReturn(Pair(jwtToken, user))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/phone/create/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.token.accessToken").value(jwtToken.accessToken))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.token.refreshToken").value(jwtToken.refreshToken.token))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.authStatus").value(ActivateType.ACCESS.toString().lowercase())
            )
        verify(authService).verifyPhone(any(), any(), any(), any())
    }

    @Test
    @DisplayName("이메일 인증번호 확인")
    fun verifyEmail() {
        val jwtToken = createJwtToken()
        val user = createUser()

        val requestBody = mapOf(
            "email" to "test@example.com",
            "verificationCode" to "123456",
            "appToken" to "testToken",
            "deviceId" to "testDeviceId",
            "provider" to "testProvider"
        )
        whenever(authService.verifyEmail(any(), any(), any(), any()))
            .thenReturn(Pair(jwtToken, user))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/email/create/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.token.accessToken").value(jwtToken.accessToken))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.token.refreshToken").value(jwtToken.refreshToken.token))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.authStatus").value(ActivateType.ACCESS.toString().lowercase())
            )
        verify(authService).verifyEmail(any(), any(), any(), any())
    }

    @Test
    @DisplayName("휴대폰 변경을 위한 인증번호 전송")
    fun sendPhoneVerificationForUpdate() {
        val requestBody = mapOf(
            "countryCode" to "82",
            "phoneNumber" to "010-1234-5678"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/phone/update/send")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "testUserId")
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
        verify(authService).sendPhoneVerificationForUpdate(any(), any())
    }

    @Test
    @DisplayName("이메일 변경을 위한 인증번호 전송")
    fun sendEmailVerificationForUpdate() {
        val requestBody = mapOf(
            "email" to "test@example.com"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/email/update/send")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "testUserId")
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
        verify(authService).sendEmailVerificationForUpdate(any(), any())
    }

    @Test
    @DisplayName("이메일 변경을 위한 인증번호 확인")
    fun verifyEmailForUpdate() {
        val requestBody = mapOf(
            "email" to "test@example.com",
            "verificationCode" to "123456"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/email/update/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "testUserId")
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
        verify(authService).verifyEmailForUpdate(any(), any(), any())
    }

    @Test
    @DisplayName("휴대폰 변경을 위한 인증번호 확인")
    fun verifyPhoneForUpdate() {
        val requestBody = mapOf(
            "countryCode" to "82",
            "phoneNumber" to "010-1234-5678",
            "verificationCode" to "123456"
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/phone/update/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "testUserId")
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessResponse(result)
        verify(authService).verifyPhoneForUpdate(any(), any(), any())
    }

    @Test
    @DisplayName("로그아웃")
    fun logout() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
        )
        performCommonSuccessResponse(result)
        verify(authService).logout(any())
    }

    @Test
    @DisplayName("토큰 갱신")
    fun refreshAccessToken() {
        val jwtToken = createJwtToken()
        whenever(authService.refreshAccessToken(any()))
            .thenReturn(jwtToken)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value(jwtToken.accessToken))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").value(jwtToken.refreshToken.token))
        verify(authService).refreshAccessToken(any())
    }

}