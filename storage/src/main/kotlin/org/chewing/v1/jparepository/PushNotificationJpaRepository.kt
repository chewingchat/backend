package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.PushNotificationJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PushNotificationJpaRepository : JpaRepository<PushNotificationJpaEntity, String> {
    fun findByDeviceIdAndDeviceProvider(user: String, deviceId: String): Optional<PushNotificationJpaEntity>
}