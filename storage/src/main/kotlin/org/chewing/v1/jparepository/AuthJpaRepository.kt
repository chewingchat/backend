package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.AuthJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
@Repository
interface AuthJpaRepository: JpaRepository<AuthJpaEntity, String> {

    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.email e JOIN FETCH a.user u WHERE e.email = :email")
    fun findByEmail(
        @Param("email") email: String
    ): Optional<AuthJpaEntity>

    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.phoneNumber p JOIN FETCH a.user u WHERE p.phoneNumber = :phoneNumber AND p.countryCode = :countryCode")
    fun findByPhoneNumber(
        @Param("phoneNumber") phoneNumber: String,
        @Param("countryCode") countryCode: String
    ): Optional<AuthJpaEntity>
}