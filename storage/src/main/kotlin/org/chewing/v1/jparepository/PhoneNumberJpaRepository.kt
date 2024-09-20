package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.PhoneNumberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PhoneNumberJpaRepository : JpaRepository<PhoneNumberJpaEntity, String> {
    fun findByUserId(userId: String): Optional<PhoneNumberJpaEntity>
}