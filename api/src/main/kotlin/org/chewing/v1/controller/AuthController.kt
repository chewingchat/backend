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

    @PostMapping("/phone/create/send")
    fun sendPhoneVerification(@RequestBody request: VerificationRequest.Phone): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendPhoneVerification(request.toPhoneNumber())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/phone/create/verify")
    fun verifyPhone(@RequestBody request: LoginRequest.Phone): SuccessResponseEntity<AuthInfoResponse> {
        val (token, user) = authService.verifyPhone(
            request.toPhoneNumber(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(AuthInfoResponse.of(token, user))
    }

    @PostMapping("/email/create/send")
    fun sendEmailVerification(@RequestBody request: VerificationRequest.Email): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendEmailVerification(request.email)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/create/verify")
    fun verifyEmail(@RequestBody request: LoginRequest.Email): SuccessResponseEntity<AuthInfoResponse> {
        val (token, user) = authService.verifyEmail(
            request.toAddress(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(AuthInfoResponse.of(token, user))
    }

    @PostMapping("/phone/update/send")
    fun sendPhoneVerification(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationRequest.Phone
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.sendPhoneVerificationForUpdate(userId, request.toPhoneNumber())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/update/send")
    fun sendEmailVerification(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationRequest.Email
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.sendEmailVerificationForUpdate(userId, request.toAddress())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/phone/update/verify")
    fun changePhone(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationCheckRequest.Phone
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.verifyPhoneForUpdate(userId, request.toPhoneNumber(), request.toVerificationCode())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/update/verify")
    fun changeEmail(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationCheckRequest.Email
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.verifyEmailForUpdate(userId, request.email, request.verificationCode)
        return ResponseHelper.successOnly()
    }


    @DeleteMapping("/logout")
    fun logout(@RequestHeader("Authorization") accessToken: String): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.logout(accessToken)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/refresh")
    fun refreshAccessToken(@RequestHeader("Authorization") refreshToken: String): SuccessResponseEntity<TokenResponse> {
        val token = authService.refreshAccessToken(refreshToken)
        return ResponseHelper.success(TokenResponse.of(token))
    }
}