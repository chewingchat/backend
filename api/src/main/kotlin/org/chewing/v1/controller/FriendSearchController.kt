package org.chewing.v1.controller

import org.chewing.v1.dto.request.FriendSearchRequest
import org.chewing.v1.dto.response.search.FriendSearchHistoryResponse
import org.chewing.v1.dto.response.search.FriendSearchResultResponse
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.service.SearchService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friend/search")
class FriendSearchController(
    private val searchService: SearchService,

    ) {
    @GetMapping("")
    fun searchFriend(
        @RequestAttribute("userId") userId: String,
        @RequestParam("keyword") keyword: String
    ): SuccessResponseEntity<FriendSearchResultResponse> {
        val friends = searchService.searchFriends(userId, keyword)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendSearchResultResponse.ofList(friends))
    }

    @PostMapping("")
    fun addSearchFriend(
        @RequestAttribute("userId") userId: String,
        @RequestBody friendRequest: FriendSearchRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val friendSearch = friendRequest.toSearchFriend()
        searchService.addSearchedFriend(userId, friendSearch)
        //성공 응답 200 반환
        return ResponseHelper.successCreate()
    }

    @GetMapping("/recent")
    fun getSearchFriendHistory(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<FriendSearchHistoryResponse> {
        val friends = searchService.getSearchedFriend(userId)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendSearchHistoryResponse.ofList(friends))
    }
}