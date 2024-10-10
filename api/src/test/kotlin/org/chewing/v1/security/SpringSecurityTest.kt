package org.chewing.v1.security

import org.chewing.v1.config.SecurityConfig
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.chewing.v1.service.AuthService
import org.chewing.v1.service.FeedService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig::class)
@AutoConfigureMockMvc
class SpringSecurityTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {
    @Test
    fun `토큰 없이 들어온다면 에러 발생`() {
        mockMvc.perform(
            get("/api/private")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `토큰이 유효하지 않은 토큰이라면 에러 방생`() {
        mockMvc.perform(
            get("/api/private")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }
}
