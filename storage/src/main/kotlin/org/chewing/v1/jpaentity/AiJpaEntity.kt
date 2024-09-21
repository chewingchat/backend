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
internal class AiJpaEntity(
    @Id
    @Column(name = "ai_id", nullable = false)
    val aiId: String,

    @Column(name = "function_type", nullable = false)
    val functionType: String,

    val userId: String
) : BaseEntity() {
}