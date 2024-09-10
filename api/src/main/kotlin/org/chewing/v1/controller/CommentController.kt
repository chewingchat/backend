package org.chewing.v1.controller

import org.chewing.v1.dto.request.CommentRequest
import org.chewing.v1.dto.response.FeedCommentsResponse
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.CommentService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feed/comment")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("")
    fun addFeedComment(
        @RequestHeader("userId") userId: String,
        @RequestBody commentRequest: CommentRequest.AddCommentRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val (feedId, comment) = commentRequest
        commentService.addFeedComment(User.UserId.of(userId), Feed.FeedId.of(feedId), comment)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @DeleteMapping("")
    fun deleteFeedComment(
        @RequestHeader("userId") userId: String,
        @RequestBody request: List<CommentRequest.DeleteCommentRequest>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        commentService.deleteFeedComment(User.UserId.of(userId), request.map{it.toCommentId()})
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @GetMapping("/{feedId}")
    fun getFeedComments(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FeedCommentsResponse> {
        val feedComments = commentService.getFeedComments(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(FeedCommentsResponse.of(feedComments))
    }
}