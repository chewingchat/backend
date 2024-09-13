package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.ConfirmPhoneNumberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*


interface ConfirmPhoneNumberJpaRepository : JpaRepository<ConfirmPhoneNumberJpaEntity, String> {
    // AuthJpaRepository가 아닌 ConfirmPhoneNumberJpaRepository로 변경
    @Query("SELECT a FROM ConfirmPhoneNumberJpaEntity a JOIN FETCH a.phoneNumber p WHERE p.phoneNumber = :phoneNumber AND p.countryCode = :countryCode")
    fun findWithPhoneNumber(
        @Param("phoneNumber") phoneNumber: String,
        @Param("countryCode") countryCode: String
    ): Optional<ConfirmPhoneNumberJpaEntity>
}