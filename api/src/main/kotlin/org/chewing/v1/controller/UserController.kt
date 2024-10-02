package org.chewing.v1.controller

import org.chewing.v1.dto.request.UserRequest
import org.chewing.v1.dto.request.UserStatusRequest
import org.chewing.v1.dto.response.user.UserProfileResponse
import org.chewing.v1.dto.response.user.UserStatusesResponse
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.response.SuccessCreateResponse
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
    @GetMapping("/profile")
    fun getUserProfile(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<UserProfileResponse> {
        val userProfile = userService.getUserProfile(userId)
        return ResponseHelper.success(UserProfileResponse.of(userProfile))
    }

    @PostMapping("/access")
    fun makeAccess(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserRequest.UpdateProfile,
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.makeAccess(userId, request.toUserContent())
        return ResponseHelper.successOnly()
    }

    /**
     * @param file: 프로파일 이미지를 MultipartFile로 받습니다.
     * 로그인한 사용자의 프로파일 이미지를 변경합니다.
     */
    @PostMapping("/image")
    fun changeUserImage(
        @RequestPart("file") file: MultipartFile,
        @RequestAttribute("userId") userId: String,
        @RequestParam("category") category: FileCategory
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val convertedFile = FileUtil.convertMultipartFileToFileData(file)
        userService.updateProfileImage(convertedFile, userId, category)
        return ResponseHelper.successOnly()
    }

    @PutMapping("/status/select")
    fun changeProfileSelectedStatus(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserStatusRequest.Update
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.selectUserStatus(userId, request.statusId)
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("/status/select")
    fun deleteProfileSelectedStatus(
        @RequestAttribute("userId") userId: String,
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.deselectUserStatus(userId)
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("/status")
    fun deleteProfileStatus(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: List<UserStatusRequest.Delete>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.deleteUserStatuses(request.map { it.statusId })
        return ResponseHelper.successOnly()
    }

    @PostMapping("/status")
    fun addProfileStatus(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserStatusRequest.Add
    ): SuccessResponseEntity<SuccessCreateResponse> {
        userService.addUserStatus(userId, request.message, request.emoji)
        return ResponseHelper.successCreate()
    }


    @PutMapping("/name")
    fun changeName(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserRequest.UpdateName
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.updateName(userId, request.toUserName())
        return ResponseHelper.successOnly()
    }

    @PutMapping("birth")
    fun changeBirth(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: UserRequest.UpdateBirth
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.updateBirth(userId, request.toBirth())
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("")
    fun deleteUser(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        userService.deleteUser(userId)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/status")
    fun getUserStatus(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<UserStatusesResponse> {
        val userStatuses = userService.getUserStatuses(userId)
        return ResponseHelper.success(UserStatusesResponse.of(userStatuses))
    }

    @PostMapping("/tts")
    fun changeTTS(
        @RequestAttribute("userId") userId: String,
        @RequestPart("file") file: MultipartFile,
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val convertedFile = FileUtil.convertMultipartFileToFileData(file)
        userService.changeTTS(userId, convertedFile)
        return ResponseHelper.successOnly()
    }
}