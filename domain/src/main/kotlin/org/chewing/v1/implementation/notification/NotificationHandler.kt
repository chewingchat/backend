package org.chewing.v1.implementation.notification

import org.chewing.v1.implementation.feed.feed.FeedReader
import org.chewing.v1.implementation.user.user.UserReader
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class NotificationHandler(
    private val userReader: UserReader,
    private val feedReader: FeedReader,
    private val notificationGenerator: NotificationGenerator,
    private val notificationSender: NotificationSender
) {
    fun handleCommentNotification(userId: String, feedId: String) {
        val user = userReader.read(userId)
        val feedInfo = feedReader.readInfo(feedId)
        val pushTokens = userReader.readsPushToken(feedInfo.userId)
        val (fcmList, apnsList) = notificationGenerator.generateCommentNotification(user, pushTokens, feedInfo)
        notificationSender.send(fcmList, apnsList)
    }
}