package org.chewing.v1.implementation.facade

import org.chewing.v1.model.User
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendFeed
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.FriendService
import org.springframework.stereotype.Service

@Service
class FriendFacade(
    private val friendService: FriendService,
    private val feedService: FeedService
) {
    fun getFriendDetail(userId: User.UserId, friendId: User.UserId): Pair<Friend, List<FriendFeed>> {
        // 친구 정보를 읽어옵니다.
        val friend = friendService.getFriend(userId, friendId)
        // 친구의 피드를 읽어옵니다.
        val feeds = feedService.getFriendFeedsFull(friendId)
        // 결과를 반환합니다.
        return Pair(friend, feeds)
    }

}