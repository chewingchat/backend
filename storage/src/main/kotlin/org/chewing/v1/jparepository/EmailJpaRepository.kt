package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.EmailJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmailJpaRepository : JpaRepository<EmailJpaEntity, String> {
    fun findByUserId(userId: String): Optional<EmailJpaEntity>
}