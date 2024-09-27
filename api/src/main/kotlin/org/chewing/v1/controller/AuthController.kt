package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
import org.chewing.v1.dto.response.TokenResponse
import org.chewing.v1.dto.response.auth.EmailId
import org.chewing.v1.dto.response.auth.PhoneNumberId
import org.chewing.v1.implementation.JwtTokenProvider
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.AuthService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/phone/verify/send")
    fun sendPhoneVerification(@RequestBody request: VerificationRequest.Phone): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendPhoneVerification(request.toPhoneNumber())
        return ResponseHelper.successOnly()
    }

    // 넘길때 이와 같이 객체로 넘기면 좋을 것 같아요
    @PostMapping("/phone/verify/login")
    fun verifyPhoneAndLogin(@RequestBody request: LoginRequest.Phone): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyPhoneAndLogin(
            request.toPhoneNumber(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/email/verify/send")
    fun sendEmailVerification(@RequestBody request: VerificationRequest.Email): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        authService.sendEmailVerification(request.email)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/verify/login")
    fun verifyEmailAndLogin(@RequestBody request: LoginRequest.Email): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.verifyEmailAndLogin(
            request.toAddress(),
            request.toVerificationCode(),
            request.toAppToken(),
            request.toDevice()
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/email/verify/signup")
    fun verifyEmailForSignup(@RequestBody request: VerificationCheckRequest.Email): SuccessResponseEntity<EmailId> {
        val emailId = authService.verifyEmailAndSignup(
            request.toAddress(),
            request.toVerificationCode(),
        )
        return ResponseHelper.success(EmailId(emailId))
    }

    @PostMapping("/phone/verify/signup")
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
        // Pair를 Map으로 변환
        return ResponseHelper.success(TokenResponse.of(access, refresh))

        //oAuth방식 사용

    }

    @PostMapping("/phone/verify/send/update")
    fun sendPhoneVerification(
        @RequestHeader("Authorization") accessToken: String,
        @RequestBody request: VerificationRequest.Phone
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
        authService.sendPhoneVerificationForUpdate(userId, request.toPhoneNumber())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/phone/verify/check/update")
    fun verifyPhoneAndUpdate(
        @RequestHeader("Authorization") accessToken: String,
        @RequestBody request: VerificationCheckRequest.Phone
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
        authService.verifyPhoneForUpdate(userId, request.toPhoneNumber(), request.toVerificationCode())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/verify/send/update")
    fun sendEmailVerification(
        @RequestHeader("Authorization") accessToken: String,
        @RequestBody request: VerificationRequest.Email
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
        authService.sendEmailVerificationForUpdate(userId, request.toAddress())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/email/verify/check/update")
    fun verifyEmailAndUpdate(
        @RequestHeader("Authorization") accessToken: String,
        @RequestBody request: VerificationCheckRequest.Email
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
        authService.verifyEmailForUpdate(userId, request.email, request.verificationCode)
        return ResponseHelper.successOnly()
    }
    @PostMapping("/email/signup")
    fun signUpEmail(
        @RequestPart(value = "profileImage") profileImage: MultipartFile?, // 프로필 이미지를 선택적으로 처
        @RequestBody request: SignupRequest.Email
    ): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.signUpWithEmail(
            request.toEmailId(),
            request.toUserContent(),
            request.toDevice(),
            request.toPushToken(),
            profileImage // 프로필 이미지 전달
        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    @PostMapping("/phone/signup")
    fun signUpPhone(
        @RequestPart(value = "profileImage") profileImage: MultipartFile?, // 프로필 이미지를 선택적으로 처
        @RequestBody request: SignupRequest.Phone
    ): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.signUpWithPhone(
            request.toPhoneId(),
            request.toUserContent(),
            request.toDevice(),
            request.toPushToken(),
            profileImage // 프로필 이미지 전달


        )
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    // 회원가입 부분 추가

    // 구글 OAuth 인증 및 회원가입
    @PostMapping("/google/signup")
    fun googleSignUp(
        @RequestPart(value = "profileImage") profileImage: MultipartFile?,
        @RequestBody request: OAuthSignupRequest): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.signUpWithGoogle(request.oauthToken, profileImage)
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }

    // 애플 OAuth 인증 및 회원가입
    @PostMapping("/apple/signup")
    fun appleSignUp(
        @RequestPart(value = "profileImage") profileImage: MultipartFile?,
        @RequestBody request: OAuthSignupRequest): SuccessResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = authService.signUpWithApple(request.oauthToken, profileImage)
        return ResponseHelper.success(TokenResponse.of(accessToken, refreshToken))
    }


}