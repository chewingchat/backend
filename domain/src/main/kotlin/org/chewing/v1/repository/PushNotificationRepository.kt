package org.chewing.v1.repository

import org.springframework.stereotype.Repository

@Repository
interface PushNotificationRepository {
    fun deleteAllByUserId(userId: String)
}