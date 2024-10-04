package org.chewing.v1.implementation.notification

import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class NotificationProcessor(
    private val userReader: UserReader,
    private val feedReader: FeedReader,
    private val notificationGenerator: NotificationGenerator,
    private val notificationSender: NotificationSender
) {
    fun processCommentNotification(user: User, feedId: String) {
        val feedInfo = feedReader.readInfo(feedId)
        val pushTokens = userReader.readsPushToken(feedInfo.userId)
        val (fcmList, apnsList) = notificationGenerator.generateCommentNotification(user, pushTokens, feedInfo)
        notificationSender.send(fcmList, apnsList)
    }
}