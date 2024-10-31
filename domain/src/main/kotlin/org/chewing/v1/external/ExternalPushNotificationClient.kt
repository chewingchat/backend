package org.chewing.v1.external

import org.chewing.v1.model.notification.Notification

interface ExternalPushNotificationClient {
    fun sendFcmNotification(notification: Notification)
}