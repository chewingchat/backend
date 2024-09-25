package org.chewing.v1.controller

import org.chewing.v1.dto.request.UserRequest
import org.chewing.v1.dto.request.UserStatusRequest
import org.chewing.v1.dto.request.VerificationCheckRequest
import org.chewing.v1.dto.request.VerificationRequest
import org.chewing.v1.dto.response.emoticon.EmoticonPacksResponse
import org.chewing.v1.util.FileUtil
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.AuthService
import org.chewing.v1.service.UserService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val authService: AuthService
) {
    /**
     * @param file: 프로파일 이미지를 MultipartFile로 받습니다.
     * 로그인한 사용자의 프로파일 이미지를 변경합니다.
     */
    @PostMapping("/profile/upload")
    fun changeProfileImage(
        @RequestPart("file") file: MultipartFile,
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val convertedFile = FileUtil.convertMultipartFileToFile(file)
        userService.updateUserImage(convertedFile, userId)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/profile/status")
    fun changeProfileStatus(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserStatusRequest
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        return ResponseHelper.successOnly()
    }

    @PostMapping("/profile/phone/send")
    fun sendPhoneVerification(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationRequest.Phone
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.sendPhoneVerificationForUpdate(userId, request.toPhoneNumber())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/profile/email/send")
    fun sendEmailVerification(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationRequest.Email
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.sendEmailVerificationForUpdate(userId, request.toAddress())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/profile/phone/check")
    fun changePhone(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationCheckRequest.Phone
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.verifyPhoneForUpdate(userId, request.toPhoneNumber(), request.toVerificationCode())
        return ResponseHelper.successOnly()
    }

    @PostMapping("/profile/email/check")
    fun changeEmail(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: VerificationCheckRequest.Email
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        authService.verifyEmailForUpdate(userId, request.email, request.verificationCode)
        return ResponseHelper.successOnly()
    }

    @PutMapping("/profile/name")
    fun changeName(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserRequest.UpdateName
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.updateUserName(userId, request.toUserName())
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("")
    fun deleteUser(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.deleteUser(userId)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/emoticon")
    fun getEmoticonPacks(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<EmoticonPacksResponse> {
        val emoticonPacks = userService.findOwnedEmoticonPacks(userId)
        return ResponseHelper.success(EmoticonPacksResponse.of(emoticonPacks))
    }
}