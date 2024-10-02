package org.chewing.v1.implementation.notification

import org.chewing.v1.external.ExternalNotificationClient
import org.chewing.v1.model.notification.Notification
import org.springframework.stereotype.Component

@Component
class NotificationSender(
    private val externalNotificationClient: ExternalNotificationClient
) {
    fun send(fcmList: List<Notification>, apnsList: List<Notification>) {
        externalNotificationClient.sendApnsNotification(apnsList)
        externalNotificationClient.sendFcmNotifications(fcmList)
    }
}