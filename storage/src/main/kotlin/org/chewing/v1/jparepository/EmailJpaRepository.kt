package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface EmailJpaRepository : JpaRepository<EmailJpaEntity, String> {
    fun findByEmailAddress(emailAddress: String): Optional<EmailJpaEntity>

    fun findByEmailId(emailId: String): Optional<EmailJpaEntity>
}