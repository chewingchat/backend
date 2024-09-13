package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.ConfirmEmailJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ConfirmEmailJpaRepository : JpaRepository<ConfirmEmailJpaEntity, String> {

    // 이메일로 ConfirmEmail 정보를 가져오는 쿼리
    @Query("SELECT e FROM ConfirmEmailJpaEntity e WHERE e.email.emailAddress = :email")
    fun findWithEmail(@Param("email") email: String): Optional<ConfirmEmailJpaEntity>
}