package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
import org.chewing.v1.dto.response.TokenResponse
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.AuthService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/phone/verify/send")
    fun sendPhoneVerification(@RequestBody request: PhoneVerificationRequest): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendPhoneVerification(request.phoneNumber, request.countryCode)
        return ResponseHelper.successOnly()
    }

    // 넘길때 이와 같이 객체로 넘기면 좋을 것 같아요
    @PostMapping("/phone/verify/login")
    fun verifyPhoneAndLogin(@RequestBody request: PhoneLoginRequest): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyPhoneAndLogin(
            request.toPhone(),
            request.toPushToken(),
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/email/verify/send")
    fun sendEmailVerification(@RequestBody request: EmailVerificationRequest): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendEmailVerification(request.email)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/verify/login")
    fun verifyEmailAndLogin(@RequestBody request: EmailLoginRequest): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyEmailAndLogin(
            request.toEmail(),
            request.toPushToken(),
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/email/verify/signup")
    fun verifyEmailAndSignup(@RequestBody request: EmailSignupRequest): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyEmailAndSignup(
            request.toEmail(),
            request.toPushToken()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/phone/verify/signup")
    fun verifyPhoneAndSignup(@RequestBody request: PhoneSignupRequest): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyPhoneAndSignup(
            request.toPhone(),
            request.toPushToken(),
            request.toUser()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }



    @DeleteMapping("/logout")
    fun logout(@RequestHeader("Authorization") accessToken: String): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.logout(accessToken)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/token/refresh")
    fun refreshAccessToken(@RequestHeader("Authorization") refreshToken: String): ResponseEntity<Map<String, String>> {
        val tokens = authService.refreshAccessToken(refreshToken)
        // Pair를 Map으로 변환
        val tokenMap = mapOf("access_token" to tokens.first, "refresh_token" to tokens.second)
        return ResponseEntity.ok(tokenMap)

        //oAuth방식 사용

    }


}