package org.chewing.v1.external

import org.chewing.v1.model.notification.Notification

interface ExternalNotificationClient {
    fun sendApnsNotification(notificationList: List<Notification>)
    fun sendFcmNotifications(notificationList: List<Notification>)
}