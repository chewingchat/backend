package org.chewing.v1.controller

import org.chewing.v1.dto.response.main.MainFriendCardsResponse
import org.chewing.v1.dto.response.main.MainFriendListResponse
import org.chewing.v1.implementation.facade.MainFacade
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/main")
class MainController(
    private val mainFacade: MainFacade
) {
    @GetMapping("/friend/card")
    fun getMainPageFriendCards(
        @RequestAttribute("userId") userId: String,
        @RequestParam("sort") sort: String
    ): SuccessResponseEntity<MainFriendCardsResponse> {
        val sortCriteria = SortCriteria.valueOf(sort.uppercase())
        val (user, userStatus, friends) = mainFacade.getMainPage(userId, sortCriteria)
        //성공 응답 200 반환
        return ResponseHelper.success(MainFriendCardsResponse.ofList(user, userStatus, friends))
    }

    @GetMapping("/friend/list")
    fun getMainPageFriendList(
        @RequestAttribute("userId") userId: String,
        @RequestParam("sort") sort: String
    ): SuccessResponseEntity<MainFriendListResponse> {
        val sortCriteria = SortCriteria.valueOf(sort.uppercase())
        val (user, userStatus, friends) = mainFacade.getMainPage(userId, sortCriteria)
        //성공 응답 200 반환
        return ResponseHelper.success(MainFriendListResponse.ofList(user, userStatus, friends))
    }
}