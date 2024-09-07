package org.chewing.v1.jpaentity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "logged_in", schema = "chewing")
class LoggedInEntity(
    @Id
    @Column(name = "logged_in_id")
    val loggedInId: String = UUID.randomUUID().toString(),

    @Column(name = "refresh_token")
    val refreshToken: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auth_id", nullable = false)
    val auth: AuthJpaEntity,

    @Column(name = "device_id")
    val deviceId: String
) {
}