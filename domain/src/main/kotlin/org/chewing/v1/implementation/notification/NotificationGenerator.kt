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
    ): Pair<List<Notification>, List<Notification>> {
        val groupedByProvider = pushTokens.groupBy { it.device.provider }
        val fcmList = groupedByProvider[PushToken.Provider.FCM] ?: emptyList()
        val apnsList = groupedByProvider[PushToken.Provider.APNS] ?: emptyList()
        val notificationFcmList = fcmList.map {
            Notification.makeCommentNotification(sourceUser, it, feedInfo)
        }
        val notificationApnsList = apnsList.map {
            Notification.makeCommentNotification(sourceUser, it, feedInfo)
        }
        return Pair(notificationFcmList, notificationApnsList)
    }
    fun generateMessageNotification(
        sourceUser: User,
        pushTokens: List<PushToken>,
        message: ChatMessage
    ): Pair<List<Notification>, List<Notification>> {
        val groupedByProvider = pushTokens.groupBy { it.device.provider }
        val fcmList = groupedByProvider[PushToken.Provider.FCM] ?: emptyList()
        val apnsList = groupedByProvider[PushToken.Provider.APNS] ?: emptyList()
        val notificationFcmList = fcmList.map {
            Notification.makeMessageNotification(sourceUser, it, message)
        }
        val notificationApnsList = apnsList.map {
            Notification.makeMessageNotification(sourceUser, it, message)
        }
        return Pair(notificationFcmList, notificationApnsList)
    }
}