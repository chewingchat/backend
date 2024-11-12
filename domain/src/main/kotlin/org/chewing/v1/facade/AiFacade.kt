package org.chewing.v1.facade

import org.chewing.v1.model.ai.DateTarget
import org.chewing.v1.service.ai.AiService
import org.chewing.v1.service.feed.FeedService
import org.chewing.v1.service.friend.FriendShipService
import org.springframework.stereotype.Component

@Component
class AiFacade(
    private val feedService: FeedService,
    private val aiService: AiService,
    private val friendShipService: FriendShipService,
) {
    fun getAiRecentSummary(userId: String, friendId: String, targetDate: DateTarget) {
        val friendName = friendShipService.getFriendName(userId, friendId)
        val feeds = feedService.getFriendFulledFeeds(friendId, targetDate)
        aiService.getAiRecentSummary(friendName, feeds)
    }
}
