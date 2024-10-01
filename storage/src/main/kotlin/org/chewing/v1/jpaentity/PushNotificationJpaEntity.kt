package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
import java.util.*

@Entity
@Table(name = "push_notification", schema = "chewing")
internal class PushNotificationJpaEntity(
    @Id
    @Column(name = "push_notification_id")
    private val pushId: String = UUID.randomUUID().toString(),

    private val appToken: String,

    private val deviceId: String,

    @Enumerated(EnumType.STRING)
    private var provider: PushToken.Provider,

    private val userId: String,
) {
    companion object {
        fun generate(
            appToken: String,
            device: PushToken.Device,
            user: User
        ): PushNotificationJpaEntity {
            return PushNotificationJpaEntity(
                appToken = appToken,
                deviceId = device.deviceId,
                provider = device.provider,
                userId = user.userId
            )
        }
    }
}