package org.chewing.v1.controller

import org.chewing.v1.dto.request.FeedRequest
import org.chewing.v1.dto.request.LikesRequest
import org.chewing.v1.dto.response.FriendFeedResponse
import org.chewing.v1.dto.response.MyFeedResponse
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.FeedService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feed")
class FeedController(
    private val feedService: FeedService
) {
    @GetMapping("/friend/{feedId}")
    fun getFriendFeed(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FriendFeedResponse> {
        val friendFeed = feedService.getFriendFeed(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendFeedResponse.of(friendFeed))
    }

    @GetMapping("/user/{feedId}")
    fun getUserFeed(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<MyFeedResponse> {
        val feed = feedService.getFeed(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(MyFeedResponse.of(feed))
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
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val feedId = request.toFeedId()
        feedService.deleteFeedLikes(User.UserId.of(userId), feedId)
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("")
    fun deleteFeed(
        @RequestHeader("userId") userId: String,
        @RequestBody request: FeedRequest.Delete
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        feedService.deleteFeed(User.UserId.of(userId), request.toFeedId())
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }
}