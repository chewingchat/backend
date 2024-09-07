package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@DynamicInsert
@Entity
@Table(name = "ai", schema = "chewing")
class AiJpaEntity(
    @Id
    @Column(name = "ai_id", nullable = false)
    val aiId: String,

    @Column(name = "function_type", nullable = false)
    val functionType: String,

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val userId: UserJpaEntity
) : BaseEntity() {
}