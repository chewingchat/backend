package org.chewing.v1.controller

import org.chewing.v1.dto.response.MyFriendCardsResponse
import org.chewing.v1.dto.response.MyFriendListResponse
import org.chewing.v1.dto.response.MyCommentResponse
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.implementation.facade.MyFacade
import org.chewing.v1.service.CommentService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/my")
class MyController(
    private val myFacade: MyFacade,
    private val commentService: CommentService
) {
    @GetMapping("/friend/card")
    fun getMyProfileFriendCards(
        @RequestHeader("userId") userId: String,
        @RequestParam("sort") sort: String
    ): SuccessResponseEntity<MyFriendCardsResponse> {
        val sortCriteria = SortCriteria.valueOf(sort.uppercase())
        val (user, friends) = myFacade.getMyFriends(User.UserId.of(userId), sortCriteria)
        //성공 응답 200 반환
        return ResponseHelper.success(MyFriendCardsResponse.ofList(user, friends))
    }

    @GetMapping("/friend/list")
    fun getMyProfileFriendList(
        @RequestHeader("userId") userId: String,
        @RequestParam("sort") sort: String
    ): SuccessResponseEntity<MyFriendListResponse> {
        val sortCriteria = SortCriteria.valueOf(sort.uppercase())
        val (user, friends) = myFacade.getMyFriends(User.UserId.of(userId), sortCriteria)
        //성공 응답 200 반환
        return ResponseHelper.success(MyFriendListResponse.ofList(user, friends))
    }

    @GetMapping("/comment")
    fun getMyCommentedFeed(
        @RequestHeader("userId") userId: String,
    ): SuccessResponseEntity<MyCommentResponse> {
        val feedCommentsWithFeed = commentService.getFeedUserCommented(User.UserId.of(userId))
        //성공 응답 200 반환
        return ResponseHelper.success(MyCommentResponse.of(feedCommentsWithFeed))
    }
}