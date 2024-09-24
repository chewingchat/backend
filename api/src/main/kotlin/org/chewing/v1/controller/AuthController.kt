package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
import org.chewing.v1.dto.response.auth.AuthInfoResponse
import org.chewing.v1.dto.response.auth.TokenResponse
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
    private val authService: AuthService,
) {

    @PostMapping("/send/phone")
    fun sendPhoneVerification(@RequestBody request: VerificationRequest.Phone): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendPhoneVerification(request.toPhoneNumber())
        return ResponseHelper.successOnly()
    }

    // 넘길때 이와 같이 객체로 넘기면 좋을 것 같아요
    @PostMapping("/verify/phone")
    fun verifyPhone(@RequestBody request: LoginRequest.Phone): SuccessResponseEntity<AuthInfoResponse> {
        val (token, user) = authService.verifyPhone(
            request.toPhoneNumber(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(AuthInfoResponse.of(token, user))
    }

    @PostMapping("/send/email")
    fun sendEmailVerification(@RequestBody request: VerificationRequest.Email): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendEmailVerification(request.email)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/verify/email")
    fun verifyEmail(@RequestBody request: LoginRequest.Email): SuccessResponseEntity<AuthInfoResponse> {
        val (token, user) = authService.verifyEmail(
            request.toAddress(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(AuthInfoResponse.of(token, user))
    }


    @DeleteMapping("/logout")
    fun logout(@RequestHeader("Authorization") accessToken: String): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.logout(accessToken)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/token/refresh")
    fun refreshAccessToken(@RequestHeader("Authorization") refreshToken: String): SuccessResponseEntity<TokenResponse> {
        val token = authService.refreshAccessToken(refreshToken)
        return ResponseHelper.success(TokenResponse.of(token))
    }
}