package org.chewing.v1.external

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import mu.KotlinLogging
import org.chewing.v1.model.notification.Notification
import org.springframework.stereotype.Component

@Component
class ExternalPushNotificationClientImpl(
    private val fcmClient: FcmClient
) : ExternalPushNotificationClient {
    private val logger = KotlinLogging.logger {}

    @Throws(JsonParseException::class, JsonProcessingException::class)
    override fun sendFcmNotification(notification: Notification) {
        try {
            fcmClient.sendMessage(FcmMessageDto.from(notification))
        } catch (e: Exception) {
            logger.error { "알림 전송 실패: ${e.message}" }
        }
    }
}
