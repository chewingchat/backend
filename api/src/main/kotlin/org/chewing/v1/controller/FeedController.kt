package org.chewing.v1.controller

import org.chewing.v1.dto.request.FeedRequest
import org.chewing.v1.dto.request.LikesRequest
import org.chewing.v1.dto.response.feed.FriendFeedResponse
import org.chewing.v1.dto.response.feed.UserFeedResponse
import org.chewing.v1.dto.response.friend.FriendDetailResponse
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.FeedService
import org.chewing.v1.util.FileUtil
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/feed")
class FeedController(
    private val feedService: FeedService
) {
    @GetMapping("/{feedId}/friend")
    fun getFriendFeed(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FriendFeedResponse> {
        val friendFeed = feedService.getFriendFeed(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendFeedResponse.of(friendFeed))
    }

    @GetMapping("/{friendId}")
    fun getFriendFeeds(
        @RequestHeader("userId") userId: String,
        @PathVariable("friendId") friendId: String
    ): SuccessResponseEntity<FriendDetailResponse> {
        val feeds = feedService.getFriendFulledFeeds(User.UserId.of(friendId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendDetailResponse.of(feeds))
    }

    @GetMapping("/{feedId}/user")
    fun getUserFeed(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<UserFeedResponse> {
        val feed = feedService.getFeed(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(UserFeedResponse.of(feed))
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
        @RequestBody request: List<FeedRequest.Delete>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        feedService.deleteFeeds(User.UserId.of(userId), request.map { it.toFeedId() })
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @PostMapping("")
    fun createFeed(
        @RequestHeader("userId") userId: String,
        @RequestPart("files") files: List<MultipartFile>,
        @RequestParam("topic") topic: String
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val convertFiles = FileUtil.convertMultipartFilesToFiles(files)
        feedService.createFeed(User.UserId.of(userId), convertFiles, topic)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }
}