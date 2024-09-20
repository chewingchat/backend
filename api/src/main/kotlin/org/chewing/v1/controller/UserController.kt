package org.chewing.v1.controller

import org.chewing.v1.dto.request.NameUpdateRequest
import org.chewing.v1.util.FileUtil
import org.chewing.v1.model.User
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.AuthService
import org.chewing.v1.service.UserService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.http.ResponseEntity
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
        @RequestHeader("userId") userId: String
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val convertedFile = FileUtil.convertMultipartFileToFile(file)
        userService.updateUserImage(convertedFile, User.UserId.of(userId))
        return ResponseHelper.successOnly()
    }

    // 회원탈퇴로직(추가)
    @DeleteMapping("")
    fun deleteAccount(@RequestHeader("Authorization") accessToken: String): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        userService.deleteAccount(accessToken)
        return ResponseHelper.successOnly()
    }

    @PutMapping("/name")
    fun updateUserName(
        @RequestHeader("Authorization") accessToken: String,
        @RequestBody request: NameUpdateRequest
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        // 서비스 호출하여 이름 수정
        userService.updateUserName(accessToken, request.firstName, request.lastName)

        return ResponseHelper.successOnly()
    }


}