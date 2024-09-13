package org.chewing.v1.controller

import org.chewing.v1.dto.response.comment.MyCommentResponse
import org.chewing.v1.model.User
import org.chewing.v1.service.CommentService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/my")
class MyController(
    private val commentService: CommentService,
) {
    @GetMapping("/comment")
    fun getMyCommentedFeed(
        @RequestHeader("userId") userId: String,
    ): SuccessResponseEntity<MyCommentResponse> {
        val feedCommentsWithFeed = commentService.getFeedUserCommented(User.UserId.of(userId))
        //성공 응답 200 반환
        return ResponseHelper.success(MyCommentResponse.of(feedCommentsWithFeed))
    }
}