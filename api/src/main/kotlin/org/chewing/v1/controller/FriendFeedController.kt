package org.chewing.v1.controller

import org.chewing.v1.dto.request.CommentRequest
import org.chewing.v1.dto.request.LikesRequest
import org.chewing.v1.dto.response.FriendFeedResponse
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.service.FeedService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friend/feed")
class FriendFeedController(
    private val feedService: FeedService
) {
    @GetMapping("/{feedId}")
    fun getFriendFeed(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FriendFeedResponse> {
        val friendFeed = feedService.getFriendFeed(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendFeedResponse.of(friendFeed))
    }

    @PostMapping("/comment")
    fun addFeedComment(
        @RequestHeader("userId") userId: String,
        @RequestBody commentRequest: CommentRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val (feedId, comment) = commentRequest
        feedService.addFeedComment(User.UserId.of(userId), Feed.FeedId.of(feedId), comment)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @PostMapping("/like")
    fun addFeedLikes(
        @RequestHeader("userId") userId: String,
        @RequestBody request: LikesRequest.AddLikesRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val feedId = request.toFeedId()
        feedService.addFeedLikes(User.UserId.of(userId), feedId)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @DeleteMapping("/like")
    fun deleteFeedLikes(
        @RequestHeader("userId") userId: String,
        @RequestBody request: LikesRequest.DeleteLikesRequest
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val feedId = request.toFeedId()
        feedService.deleteFeedLikes(User.UserId.of(userId), feedId)
        //삭제 완료 응답 200 반환
        return ResponseHelper.successCreate()
    }
}