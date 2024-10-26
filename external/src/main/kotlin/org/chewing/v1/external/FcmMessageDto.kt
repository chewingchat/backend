package org.chewing.v1.external

import org.chewing.v1.model.notification.Notification

data class FcmMessageDto(
    val message: Message,
    val validateOnly: Boolean = true
) {
    data class Message(
        val token: String,
        val data: Map<String, String>
    )

    companion object {
        fun from(notification: Notification): FcmMessageDto {
            return FcmMessageDto(
                message = Message(
                    token = notification.pushToken.fcmToken,
                    data = mapOf(
                        "senderId" to notification.user.userId,
                        "senderFirstName" to notification.user.name.firstName,
                        "senderLastName" to notification.user.name.lastName,
                        "type" to notification.type.name,
                        "targetId" to notification.action.targetId,
                        "message" to notification.message,
                        "imageUrl" to notification.imageUrl
                    )
                )
            )
        }
    }
}
