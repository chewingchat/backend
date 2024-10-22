package org.chewing.v1.implementation.notification

import org.chewing.v1.external.ExternalNotificationClient
import org.chewing.v1.model.notification.Notification
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.stereotype.Component

@Component
class NotificationSender(
    private val externalNotificationClient: ExternalNotificationClient,
    private val asyncJobExecutor: AsyncJobExecutor
) {
    fun send(notificationList: List<Notification>) {
        asyncJobExecutor.executeAsyncJobs(notificationList) { notification ->
            externalNotificationClient.sendFcmNotification(notification)
        }
    }
}