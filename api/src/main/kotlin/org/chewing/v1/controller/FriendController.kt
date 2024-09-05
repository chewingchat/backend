package org.chewing.v1.controller

import org.chewing.v1.dto.request.*
import org.chewing.v1.dto.response.FriendCardsResponse
import org.chewing.v1.dto.response.FriendDetailResponse
import org.chewing.v1.dto.response.FriendListResponse
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.FriendService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friend")
class FriendController(
    private val friendService: FriendService,
    private val feedService: FeedService
) {
    // 오류 관련 GlobalExceptionHandler 참조 404, 401, 409번만 사용
    @PostMapping("/email")
    fun addFriendWithEmail(
        @RequestHeader("userId") userId: String,
        @RequestBody friendRequest: FriendRequest.AddWithEmail
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val userName = friendRequest.toUserName()
        friendService.addFriendWithEmail(User.UserId.of(userId), userName, friendRequest.email)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/phone")
    fun addFriendWithPhone(
        @RequestHeader("userId") userId: String,
        @RequestBody friendRequest: FriendRequest.AddWithPhone
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val userName = friendRequest.toUserName()
        friendService.addFriendWithPhone(User.UserId.of(userId), userName, friendRequest.phone)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/favorite")
    fun changeFavorite(
        @RequestHeader("userId") userId: String,
        @RequestBody friendFavoriteRequest: FriendRequest.UpdateFavorite
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val (friendId, favorite) = friendFavoriteRequest
        friendService.changeFriendFavorite(User.UserId.of(userId), User.UserId.of(friendId), favorite)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @GetMapping("/card")
    fun getFriendCards(
        @RequestHeader("userId") userId: String,
        @RequestParam("sort") sort: String
    ): SuccessResponseEntity<FriendCardsResponse> {
        val sortCriteria = SortCriteria.valueOf(sort.uppercase())
        val (user, friends) = friendService.getFriends(User.UserId.of(userId), sortCriteria)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendCardsResponse.ofList(user, friends))
    }

    @GetMapping("/list")
    fun getFriendList(
        @RequestHeader("userId") userId: String,
        @RequestParam("sort") sort: String
    ): SuccessResponseEntity<FriendListResponse> {
        val sortCriteria = SortCriteria.valueOf(sort.uppercase())
        val (user, friends) = friendService.getFriends(User.UserId.of(userId), sortCriteria)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendListResponse.ofList(user, friends))
    }

    @DeleteMapping("")
    fun deleteFriend(
        @RequestHeader("userId") userId: String,
        @RequestBody friendRequest: FriendRequest.Delete
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendId = friendRequest.friendId
        friendService.removeFriend(User.UserId.of(userId), User.UserId.of(friendId))
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @PutMapping("")
    fun changeFriendName(
        @RequestHeader("userId") userId: String,
        @RequestBody friendRequest: FriendRequest.UpdateName
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val friendName = friendRequest.toFriendName()
        val friendId = friendRequest.toFriendId()
        friendService.changeFriendName(User.UserId.of(userId), friendId, friendName)
        //생성 완료 응답 201 반환
        return ResponseHelper.successOnly()
    }

    @GetMapping("/detail/{friendId}")
    fun getFriendDetail(
        @RequestHeader("userId") userId: String,
        @PathVariable("friendId") friendId: String
    ): SuccessResponseEntity<FriendDetailResponse> {
        val (friend, friendFeeds) = friendService.getFriendDetail(User.UserId.of(userId), User.UserId.of(friendId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendDetailResponse.of(friend, friendFeeds))
    }
}