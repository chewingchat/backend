package org.chewing.v1.jpaentity

import jakarta.persistence.*;
import org.chewing.v1.jpaentity.common.BaseEntity
import org.hibernate.annotations.DynamicInsert
import java.util.*

@Entity
@DynamicInsert
@Table(name = "email", schema = "chewing")
class EmailJpaEntity(
    @Id
    @Column(name = "email_id")
    val emailId: String = UUID.randomUUID().toString(),

    @Column(name = "email")
    val email: String,

    @Column(name = "first_authorized")
    private var firstAuthorized: Boolean,
) : BaseEntity() {
}