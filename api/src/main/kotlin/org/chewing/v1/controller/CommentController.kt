package org.chewing.v1.controller

import org.chewing.v1.dto.request.CommentRequest
import org.chewing.v1.dto.response.comment.FeedCommentsResponse
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.CommentService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feed")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/comment")
    fun addFeedComment(
        @RequestHeader("userId") userId: String,
        @RequestBody request: CommentRequest.AddCommentRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        commentService.comment(
            userId,
            request.toFeedId(),
            request.toComment(),
            request.toUpdateType()
        )
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @DeleteMapping("/comment")
    fun deleteFeedComment(
        @RequestHeader("userId") userId: String,
        @RequestBody request: List<CommentRequest.DeleteCommentRequest>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        commentService.remove(
            userId,
            request.map { it.toCommentId() },
            FeedTarget.UNCOMMENTS
        )
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @GetMapping("/{feedId}/comment")
    fun getFeedComments(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FeedCommentsResponse> {
        val friendComment = commentService.fetchComment(userId, feedId)
        //성공 응답 200 반환
        return ResponseHelper.success(FeedCommentsResponse.of(friendComment))
    }
}