package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
import org.chewing.v1.dto.response.friend.FriendDetailResponse
import org.chewing.v1.implementation.facade.FriendFacade
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
    private val friendService: FriendService,
    private val friendFacade: FriendFacade
) {
    // 오류 관련 GlobalExceptionHandler 참조 404, 401, 409번만 사용
    @PostMapping("/email")
    fun addFriendWithEmail(
        @RequestHeader("userId") userId: String,
        @RequestBody request: FriendRequest.AddWithEmail
    ): SuccessResponseEntity<SuccessCreateResponse> {
        friendService.addFriend(User.UserId.of(userId), request.toUserName(), request.toContact())
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/phone")
    fun addFriendWithPhone(
        @RequestHeader("userId") userId: String,
        @RequestBody request: FriendRequest.AddWithPhone
    ): SuccessResponseEntity<SuccessCreateResponse> {
        friendService.addFriend(User.UserId.of(userId), request.toUserName(), request.toContact())
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/favorite")
    fun addFavorite(
        @RequestHeader("userId") userId: String,
        @RequestBody request: FriendRequest.UpdateFavorite
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val (friendId, favorite) = request
        friendService.changeFriendFavorite(User.UserId.of(userId), User.UserId.of(friendId), favorite)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("")
    fun deleteFriend(
        @RequestHeader("userId") userId: String,
        @RequestBody request: FriendRequest.Delete
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendId = request.friendId
        friendService.removeFriend(User.UserId.of(userId), User.UserId.of(friendId))
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @PutMapping("")
    fun changeFriendName(
        @RequestHeader("userId") userId: String,
        @RequestBody request: FriendRequest.UpdateName
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendName = request.toFriendName()
        val friendId = request.toFriendId()
        friendService.changeFriendName(User.UserId.of(userId), friendId, friendName)
        //생성 완료 응답 201 반환
        return ResponseHelper.successOnly()
    }

    @GetMapping("/{friendId}/detail")
    fun getFriendDetail(
        @RequestHeader("userId") userId: String,
        @PathVariable("friendId") friendId: String
    ): SuccessResponseEntity<FriendDetailResponse> {
        val (friend, friendFeeds) = friendFacade.getFriendDetail(User.UserId.of(userId), User.UserId.of(friendId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendDetailResponse.of(friend, friendFeeds))
    }
}