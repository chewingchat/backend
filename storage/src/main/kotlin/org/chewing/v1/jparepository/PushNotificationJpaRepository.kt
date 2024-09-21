package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.PushNotificationJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface PushNotificationJpaRepository : JpaRepository<PushNotificationJpaEntity, String> {
    fun deleteByDeviceIdAndDeviceProvider(deviceId: String, deviceProvider: String)
}