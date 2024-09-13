package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.AuthJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
@Repository
interface AuthJpaRepository : JpaRepository<AuthJpaEntity, String> {

    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.phoneNumber p JOIN FETCH a.email e JOIN FETCH a.user u WHERE p.phoneNumber = :keyword OR e.email = :keyword")
    fun findByPhoneNumberOrEmail(
        @Param("keyword") keyword: String
    ): Optional<AuthJpaEntity>


    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.email e Join Fetch a.user WHERE e.email = :email")
    fun findUserByEmail(
        @Param("email") email: String
    ): Optional<AuthJpaEntity>

    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.phoneNumber p JOIN FETCH a.user u WHERE p.phoneNumber = :phoneNumber AND p.countryCode = :countryCode")
    fun findByPhoneNumber(
        @Param("phoneNumber") phoneNumber: String,
        @Param("countryCode") countryCode: String
    ): Optional<AuthJpaEntity>


    @Query("SELECT a FROM ConfirmEmailJpaEntity a JOIN FETCH a.email e WHERE e.email = :email AND a.authorizedNumber = :verificationCode")
    fun findByEmailAndVerificationCode(
        @Param("email") email: String,
        @Param("verificationCode") verificationCode: String
    ): Optional<ConfirmEmailJpaEntity>

    // 휴대폰 번호와 인증 코드를 기반으로 인증 정보를 조회하는 쿼리
    @Query("SELECT c FROM ConfirmPhoneNumberJpaEntity c WHERE c.phoneNumber.phoneNumber = :phoneNumber AND c.authorizedNumber = :verificationCode")
    fun findByPhoneAndVerificationCode(
        @Param("phoneNumber") phoneNumber: String,
        @Param("verificationCode") verificationCode: String
    ): Optional<ConfirmPhoneNumberJpaEntity>

    // Boolean 리턴
    fun existsByPhoneNumberPhoneNumber(emailId: String): Boolean

    // 추가한 코드
    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.phoneNumber p WHERE p.phoneNumber = :phoneNumber")
    fun findAuthInfoByPhoneNumber(@Param("phoneNumber") phoneNumber: String): Optional<AuthJpaEntity>

    @Query("SELECT a FROM AuthJpaEntity a JOIN FETCH a.email e WHERE e.emailAddress = :email")
    fun findAuthInfoByEmail(
        @Param("email") email: String
    ): Optional<AuthJpaEntity>


}