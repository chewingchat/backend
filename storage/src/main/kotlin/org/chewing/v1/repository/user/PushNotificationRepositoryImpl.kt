package org.chewing.v1.repository.user

import org.chewing.v1.jpaentity.user.PushNotificationJpaEntity
import org.chewing.v1.jparepository.user.PushNotificationJpaRepository
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Repository

@Repository
internal class PushNotificationRepositoryImpl(
    private val pushNotificationJpaRepository: PushNotificationJpaRepository
) : PushNotificationRepository {
    override fun remove(device: PushToken.Device) {
        pushNotificationJpaRepository.deleteByDeviceIdAndProvider(device.deviceId, device.provider)
    }

    override fun append(device: PushToken.Device, appToken: String, user: User) {
        pushNotificationJpaRepository.save(PushNotificationJpaEntity.generate(appToken, device, user))
    }
    override fun reads(userId: String): List<PushToken> {
        val pushNotifications = pushNotificationJpaRepository.findAllByUserId(userId)
        return pushNotifications.map { it.toPushToken() }
    }
}
