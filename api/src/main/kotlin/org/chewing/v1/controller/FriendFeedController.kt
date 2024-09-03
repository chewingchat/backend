package org.chewing.v1.controller

import org.chewing.v1.dto.response.FriendFeedResponse
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.service.FriendFeedService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friend/feed")
class FriendFeedController(
    private val friendFeedService: FriendFeedService
) {
    @GetMapping("/{feedId}")
    fun getFriendFeed(
        @RequestHeader("userId") userId: String,
        @PathVariable("feedId") feedId: String
    ): SuccessResponseEntity<FriendFeedResponse> {
        val friendFeed = friendFeedService.getFriendFeed(User.UserId.of(userId), Feed.FeedId.of(feedId))
        //성공 응답 200 반환
        return ResponseHelper.success(FriendFeedResponse.of(friendFeed))
    }
}