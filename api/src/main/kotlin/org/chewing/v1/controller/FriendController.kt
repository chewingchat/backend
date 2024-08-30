package org.chewing.v1.controller

import org.chewing.v1.response.HttpResponse
import org.chewing.v1.dto.request.FriendFavoriteRequest
import org.chewing.v1.dto.request.FriendRequest
import org.chewing.v1.dto.response.FriendResponse
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.FriendService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/api/friend")
class FriendController(
    private val friendService: FriendService,
) {
    // 오류 관련 GlobalExceptionHandler 참조 404, 401, 409번만 사용
    @PostMapping("")
    fun addFriend(
        @RequestHeader("userId") userId: String,
        @RequestBody friendRequest: FriendRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val friendId = friendRequest.friendId
        val friendName = friendRequest.friendName
        friendService.addFriend(User.UserId.of(userId), User.UserId.of(friendId), friendName)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/favorite")
    fun changeFavorite(
        @RequestHeader("userId") userId: String,
        @RequestBody friendFavoriteRequest: FriendFavoriteRequest
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val (friendId, favorite) = friendFavoriteRequest
        friendService.setFavorite(User.UserId.of(userId), User.UserId.of(friendId), favorite)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @GetMapping("/list")
    fun getFriends(
        @RequestHeader("userId") userId: String,
    ): SuccessResponseEntity<List<FriendResponse>> {
        val friends = friendService.getFriends(User.UserId.of(userId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendResponse.ofList(friends))
    }

    @DeleteMapping("")
    fun deleteFriend(
        @RequestHeader("userId") userId: String,
        @RequestBody friendRequest: FriendRequest
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendId = friendRequest.friendId
        friendService.removeFriend(User.UserId.of(userId), User.UserId.of(friendId))
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }
}