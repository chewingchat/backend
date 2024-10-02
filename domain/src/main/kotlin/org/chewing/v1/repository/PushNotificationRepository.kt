package org.chewing.v1.repository

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Repository

interface PushNotificationRepository {
    fun removePushToken(device: PushToken.Device)
    fun appendPushToken(device: PushToken.Device, appToken: String, user: User)
    fun readsPushToken(userId: String): List<PushToken>
}