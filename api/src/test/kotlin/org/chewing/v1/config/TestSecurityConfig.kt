package org.chewing.v1.config

import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.chewing.v1.security.JwtAuthenticationEntryPoint
import org.mockito.kotlin.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@EnableWebSecurity
class TestSecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests { auth -> auth.anyRequest().permitAll() }
        return http.build()
    }

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider {
        return mock()
    }

    @Bean
    fun jwtAuthenticationEntryPoint(): JwtAuthenticationEntryPoint {
        return mock()
    }
}
