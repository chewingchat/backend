package org.chewing.v1.implementation.facade

import org.chewing.v1.model.User
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.feed.FriendFeed
import org.chewing.v1.service.CommentService
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.FriendService
import org.springframework.stereotype.Service

@Service
class MyFacade(
    private val commentService: CommentService,
    private val feedService: FeedService,
    private val friendService: FriendService
) {
    fun getFeedUserCommented(userId: User.UserId): List<Pair<FriendFeed, Comment>> {
        // 친구 목록을 가져옴
        val friends = friendService.getOnlyFriends(userId).associateBy { it.friend.userId }

        // 사용자가 댓글을 단 피드와 댓글을 가져옴
        val commentsWithFeedId = commentService.getUserCommented(userId)

        // 피드와 소유자 정보를 가져옴
        val feedIds = commentsWithFeedId.map { it.first }
        val friendFeeds = feedService.getFriendFeedsWithOwner(userId, feedIds, friends.values.toList())

        // friendFeeds를 맵으로 변환하여 빠르게 조회할 수 있도록 함
        val friendFeedMap = friendFeeds.associateBy { it.fulledFeed.feed.id }

        // 댓글과 피드를 매칭
        return commentsWithFeedId.mapNotNull { (feedId, comment) ->
            // 피드가 friendFeeds에 있는지 확인
            friendFeedMap[feedId]?.let { friendFeed ->
                Pair(friendFeed, comment)
            }
        }
    }
}