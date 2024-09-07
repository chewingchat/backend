package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import java.util.*

@Entity
@Table(name = "push_notification", schema = "chewing")
class PushNotificationJpaEntity(
    @Id
    @Column(name = "push_notification_id")
    val pushId: String = UUID.randomUUID().toString(),

    @Column(name = "app_token", nullable = false)
    val appToken: String,

    @Column(name = "device_id", nullable = false)
    val deviceId: String,

    @Column(name = "device_provider", nullable = false)
    val deviceProvider: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,
) {
}