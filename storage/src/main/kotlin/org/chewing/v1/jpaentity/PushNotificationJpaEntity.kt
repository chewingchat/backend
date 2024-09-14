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

    val appToken: String,

    val deviceId: String,

    val deviceProvider: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    val user: UserJpaEntity,
) {
}