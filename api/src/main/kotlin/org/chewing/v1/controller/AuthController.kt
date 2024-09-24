package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
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
    fun verifyPhoneAndLogin(@RequestBody request: LoginRequest.Phone): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyPhone(
            request.toPhoneNumber(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/send/email")
    fun sendEmailVerification(@RequestBody request: VerificationRequest.Email): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendEmailVerification(request.email)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/verify/email")
    fun verifyEmailAndLogin(@RequestBody request: LoginRequest.Email): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyEmailAndLogin(
            request.toAddress(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/signup/email")
    fun verifyEmailForSignup(@RequestBody request: VerificationCheckRequest.Email): SuccessResponseEntity<EmailId> {
        val emailId = authService.verifyEmailAndSignup(
            request.toAddress(),
            request.toVerificationCode(),
        )
        return ResponseHelper.success(EmailId(emailId))
    }

    @PostMapping("/signup/phone")
    fun verifyPhoneForSignup(@RequestBody request: VerificationCheckRequest.Phone): SuccessResponseEntity<PhoneNumberId> {
        val phoneNumberId = authService.verifyPhoneAndSignup(
            request.toPhoneNumber(),
            request.toVerificationCode(),
        )
        return ResponseHelper.success(PhoneNumberId.of(phoneNumberId))
    }


    @DeleteMapping("/logout")
    fun logout(@RequestHeader("Authorization") accessToken: String): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.logout(accessToken)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/token/refresh")
    fun refreshAccessToken(@RequestHeader("Authorization") refreshToken: String): SuccessResponseEntity<TokenResponse> {
        val (access, refresh) = authService.refreshAccessToken(refreshToken)
        return ResponseHelper.success(TokenResponse.of(access, refresh))
    }

    @PostMapping("/signup/email")
    fun signUpEmail(
        @RequestBody request: SignupRequest.Email
    ): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.signUpWithEmail(
            request.toEmailId(),
            request.toUserContent(),
            request.toDevice(),
            request.toPushToken()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/signup/phone")
    fun signUpPhone(
        @RequestBody request: SignupRequest.Phone
    ): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.signUpWithPhone(
            request.toPhoneId(),
            request.toUserContent(),
            request.toDevice(),
            request.toPushToken()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }
}