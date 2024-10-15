package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.TestDataFactory.createJwtToken
import org.chewing.v1.TestDataFactory.createUser
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.facade.AccountFacade
import org.chewing.v1.model.user.AccessStatus
import org.chewing.v1.model.auth.LoginInfo
import org.chewing.v1.service.auth.AuthService
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

@WebMvcTest(AuthController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class AuthControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var authService: AuthService

    @MockBean
    private lateinit var accountFacade: AccountFacade

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
        verify(authService).createCredential(any())
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
        verify(authService).createCredential(any())
    }

    @Test
    @DisplayName("휴대폰 인증번호 확인")
    fun verifyPhone() {
        val jwtToken = createJwtToken()
        val user = createUser()
        val loginInfo = LoginInfo.of(jwtToken, user)

        val requestBody = mapOf(
            "countryCode" to "82",
            "phoneNumber" to "010-1234-5678",
            "verificationCode" to "123456",
            "appToken" to "testToken",
            "deviceId" to "testDeviceId",
            "provider" to "apns"
        )
        whenever(accountFacade.loginAndCreateUser(any(), any(), any(), any()))
            .thenReturn(loginInfo)
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
                MockMvcResultMatchers.jsonPath("$.data.access").value(AccessStatus.ACCESS.toString().lowercase())
            )
        verify(accountFacade).loginAndCreateUser(any(), any(), any(), any())
    }

    @Test
    @DisplayName("이메일 인증번호 확인")
    fun verifyEmail() {
        val jwtToken = createJwtToken()
        val user = createUser()
        val loginInfo = LoginInfo.of(jwtToken, user)
        val requestBody = mapOf(
            "email" to "test@example.com",
            "verificationCode" to "123456",
            "appToken" to "testToken",
            "deviceId" to "testDeviceId",
            "provider" to "FCM"
        )
        whenever(accountFacade.loginAndCreateUser(any(), any(), any(), any()))
            .thenReturn(loginInfo)
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
                MockMvcResultMatchers.jsonPath("$.data.access").value(AccessStatus.ACCESS.toString().lowercase())
            )
        verify(accountFacade).loginAndCreateUser(any(), any(), any(), any())
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
        verify(accountFacade).changeCredential(any(), any(), any())
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
        verify(accountFacade).changeCredential(any(), any(), any())
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
        whenever(authService.refreshJwtToken(any()))
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
        verify(authService).refreshJwtToken(any())
    }
}