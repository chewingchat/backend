package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.FriendService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friend")
class FriendController(
    private val friendService: FriendService
) {
    // 오류 관련 GlobalExceptionHandler 참조 404, 401, 409번만 사용
    @PostMapping("/email")
    fun addFriendWithEmail(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: FriendRequest.AddWithEmail
    ): SuccessResponseEntity<SuccessCreateResponse> {
        friendService.addFriend(userId, request.toUserName(), request.toContact())
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/phone")
    fun addFriendWithPhone(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: FriendRequest.AddWithPhone
    ): SuccessResponseEntity<SuccessCreateResponse> {
        friendService.addFriend(userId, request.toUserName(), request.toContact())
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/favorite")
    fun addFavorite(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: FriendRequest.UpdateFavorite
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val (friendId, favorite) = request
        friendService.changeFriendFavorite(userId, friendId, favorite)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }
    @DeleteMapping("")
    fun deleteFriend(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: FriendRequest.Delete
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendId = request.friendId
        friendService.removeFriend(userId, friendId)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @PutMapping("")
    fun changeFriendName(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: FriendRequest.UpdateName
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendName = request.toFriendName()
        val friendId = request.toFriendId()
        friendService.changeFriendName(userId, friendId, friendName)
        //생성 완료 응답 201 반환
        return ResponseHelper.successOnly()
    }
}