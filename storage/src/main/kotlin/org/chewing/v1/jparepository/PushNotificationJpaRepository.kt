package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.PushNotificationJpaEntity
import org.chewing.v1.model.auth.PushToken
import org.springframework.data.jpa.repository.JpaRepository

internal interface PushNotificationJpaRepository : JpaRepository<PushNotificationJpaEntity, String> {
    fun deleteByDeviceIdAndProvider(deviceId: String, deviceProvider: PushToken.Provider)
    fun findAllByUserId(userId: String): List<PushNotificationJpaEntity>
}