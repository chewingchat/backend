package org.chewing.v1.external

import org.chewing.v1.model.notification.Notification
import org.springframework.stereotype.Component

@Component
class ExternalNotificationClientImpl: ExternalNotificationClient {
    override fun sendFcmNotifications(notificationList: List<Notification>) {

    }

    override fun sendApnsNotification(notificationList: List<Notification>) {

    }
}