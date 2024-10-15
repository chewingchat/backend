package org.chewing.v1.controller

import org.chewing.v1.dto.request.friend.FriendSearchRequest
import org.chewing.v1.dto.response.chat.ChatRoomIdResponse
import org.chewing.v1.dto.response.search.FriendSearchHistoryResponse
import org.chewing.v1.dto.response.search.FriendSearchResultResponse
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.facade.SearchFacade
import org.chewing.v1.service.UserService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friend/search")
class FriendSearchController(
    private val searchFacade: SearchFacade,
    private val userService: UserService
) {
    @GetMapping("")
    fun searchFriend(
        @RequestAttribute("userId") userId: String,
        @RequestParam("keyword") keyword: String
    ): SuccessResponseEntity<FriendSearchResultResponse> {
        val friends = searchFacade.searchFriends(userId, keyword)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendSearchResultResponse.ofList(friends))
    }

    @PostMapping("")
    fun addSearchKeyword(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: FriendSearchRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        userService.createSearchKeyword(userId, request.keyword)
        //성공 응답 200 반환
        return ResponseHelper.successCreate(ChatRoomIdResponse.from(roomId))
    }

    @GetMapping("/recent")
    fun getSearchHistory(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<FriendSearchHistoryResponse> {
        val friends = userService.getSearchKeywords(userId)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendSearchHistoryResponse.ofList(friends))
    }
}