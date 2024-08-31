package org.chewing.v1.controller

import org.chewing.v1.util.FileUtil
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.UserService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
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
}