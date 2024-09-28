package org.chewing.v1.controller

import org.chewing.v1.dto.request.UserRequest
import org.chewing.v1.dto.request.UserStatusRequest
import org.chewing.v1.dto.response.emoticon.EmoticonPacksResponse
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.util.FileUtil
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
    @PostMapping("/profile/activate")
    fun makeActivate(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserRequest.UpdateProfile,
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.makeActivate(userId, request.toUserContent())
        return ResponseHelper.successOnly()
    }

    /**
     * @param file: 프로파일 이미지를 MultipartFile로 받습니다.
     * 로그인한 사용자의 프로파일 이미지를 변경합니다.
     */
    @PostMapping("/profile/upload")
    fun changeProfileImage(
        @RequestPart("file") file: MultipartFile,
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val convertedFile = FileUtil.convertMultipartFileToFileData(file)
        userService.updateUserImage(convertedFile, userId, FileCategory.USER_PROFILE)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/profile/status")
    fun changeProfileStatus(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserStatusRequest
    ): SuccessResponseEntity<SuccessOnlyResponse> {
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