package org.chewing.v1.implementation.notification

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.notification.Notification
import org.chewing.v1.model.notification.NotificationAction
import org.chewing.v1.model.notification.NotificationType
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class NotificationGenerator {
    fun generateCommentNotification(
        sourceUser: User,
        pushTokens: List<PushToken>,
        feedInfo: FeedInfo
    ): List<Notification> {
        return pushTokens.map {
            Notification.makeCommentNotification(sourceUser, it, feedInfo)
        }
    }

    fun generateMessageNotification(
        sourceUser: User,
        pushTokens: List<PushToken>,
        message: ChatMessage
    ): List<Notification> {
        return pushTokens.map {
            Notification.makeMessageNotification(sourceUser, it, message)
        }
    }
}