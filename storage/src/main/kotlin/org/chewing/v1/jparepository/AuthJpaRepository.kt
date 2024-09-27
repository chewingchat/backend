package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.AuthJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
@Repository
internal interface AuthJpaRepository : JpaRepository<AuthJpaEntity, String> {
    fun findByEmailId(emailId: String): Optional<AuthJpaEntity>
    fun findByPhoneNumberId(phoneNumberId: String): Optional<AuthJpaEntity>
    fun findByUserId(userId: String): Optional<AuthJpaEntity>
}