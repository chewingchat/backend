package org.chewing.v1.controller

import org.chewing.v1.dto.request.FeedRequest
import org.chewing.v1.dto.request.LikesRequest
import org.chewing.v1.dto.response.feed.FriendFeedResponse
import org.chewing.v1.dto.response.feed.OwnedFeedResponse
import org.chewing.v1.dto.response.friend.FeedsResponse
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.FileCategory
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
    @GetMapping("/list/{friendId}")
    fun getFriendFeeds(
        @RequestAttribute("userId") userId: String,
        @PathVariable("friendId") targetUserId: String
    ): SuccessResponseEntity<FeedsResponse> {
        val feeds = feedService.getOwnedFeeds(targetUserId, FeedStatus.NOT_HIDDEN)
        //성공 응답 200 반환
        return ResponseHelper.success(FeedsResponse.of(feeds))
    }

    @GetMapping("/list")
    fun getOwnedFeeds(
        @RequestAttribute("userId") userId: String,
    ): SuccessResponseEntity<FeedsResponse> {
        val feeds = feedService.getOwnedFeeds(userId, FeedStatus.NOT_HIDDEN)
        //성공 응답 200 반환
        return ResponseHelper.success(FeedsResponse.of(feeds))
    }

    @GetMapping("/{feedId}/detail/friend")
    fun getFriendFeed(
        @RequestAttribute("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FriendFeedResponse> {
        val (feed, isLiked) = feedService.getOwnedFeed(userId, feedId, FeedStatus.NOT_HIDDEN)
        //성공 응답 200 반환
        return ResponseHelper.success(FriendFeedResponse.of(feed, isLiked))
    }

    @GetMapping("/{feedId}/detail/owned")
    fun getOwnedFeed(
        @RequestAttribute("userId") userId: String,
        @PathVariable("feedId") feedId: String,
    ): SuccessResponseEntity<OwnedFeedResponse> {
        val (feed, isLiked) = feedService.getOwnedFeed(userId, feedId, FeedStatus.NOT_HIDDEN)
        //성공 응답 200 반환
        return ResponseHelper.success(OwnedFeedResponse.of(feed, isLiked))
    }

    @GetMapping("/hide")
    fun getHiddenFeeds(
        @RequestAttribute("userId") userId: String
    ): SuccessResponseEntity<FeedsResponse> {
        val feeds = feedService.getOwnedFeeds(userId, FeedStatus.HIDDEN)
        //성공 응답 200 반환
        return ResponseHelper.success(FeedsResponse.of(feeds))
    }

    @PostMapping("/hide")
    fun hideFeeds(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: List<FeedRequest.Hide>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        feedService.hides(userId, request.map { it.toFeedId() },FeedTarget.HIDE)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("/hide")
    fun unHideFeeds(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: List<FeedRequest.Hide>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        feedService.unHides(userId, request.map { it.toFeedId() },FeedTarget.UNHIDE)
        //성공 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @PostMapping("/likes")
    fun addFeedLikes(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: LikesRequest.Add
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val feedId = request.toFeedId()
        feedService.likes(userId, feedId, request.toTarget())
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @DeleteMapping("/likes")
    fun deleteFeedLikes(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: LikesRequest.Delete
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        val feedId = request.toFeedId()
        feedService.unlikes(userId, feedId, request.toUpdateType())
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @DeleteMapping("")
    fun deleteFeeds(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: List<FeedRequest.Delete>
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        feedService.removes(userId, request.map { it.toFeedId() })
        //삭제 완료 응답 200 반환
        return ResponseHelper.successOnly()
    }

    @PostMapping("")
    fun createFeed(
        @RequestAttribute("userId") userId: String,
        @RequestPart("files") files: List<MultipartFile>,
        @RequestParam("topic") topic: String
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val convertFiles = FileUtil.convertMultipartFileToFileDataList(files)
        feedService.make(userId, convertFiles, topic, FileCategory.FEED)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }
}