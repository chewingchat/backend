package org.chewing.v1.implementation.notification

import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class NotificationProcessor(
    private val userReader: UserReader,
    private val notificationGenerator: NotificationGenerator,
    private val notificationSender: NotificationSender
) {
    fun processCommentNotification(user: User, feed: FeedInfo) {
        val pushTokens = userReader.readsPushToken(feed.userId)
        val (fcmList, apnsList) = notificationGenerator.generateCommentNotification(user, pushTokens, feed)
        notificationSender.send(fcmList, apnsList)
    }
}