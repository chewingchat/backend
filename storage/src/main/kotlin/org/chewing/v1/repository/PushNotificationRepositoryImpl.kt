package org.chewing.v1.repository

import org.chewing.v1.jpaentity.user.PushNotificationJpaEntity
import org.chewing.v1.jparepository.PushNotificationJpaRepository
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Repository

@Repository
internal class PushNotificationRepositoryImpl(
    private val pushNotificationJpaRepository: PushNotificationJpaRepository
) : PushNotificationRepository {
    override fun removePushToken(device: PushToken.Device) {
        pushNotificationJpaRepository.deleteByDeviceIdAndProvider(device.deviceId, device.provider)
    }

    override fun appendPushToken(device: PushToken.Device, appToken: String, user: User) {
        pushNotificationJpaRepository.save(PushNotificationJpaEntity.generate(appToken, device, user))
    }
    override fun readsPushToken(userId: String): List<PushToken> {
        val pushNotifications = pushNotificationJpaRepository.findAllByUserId(userId)
        return pushNotifications.map { it.toPushToken() }
    }

}