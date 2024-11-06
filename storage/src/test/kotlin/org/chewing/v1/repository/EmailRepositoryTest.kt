package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.auth.EmailJpaRepository
import org.chewing.v1.repository.auth.EmailRepositoryImpl
import org.chewing.v1.repository.support.EmailProvider
import org.chewing.v1.repository.support.JpaDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class EmailRepositoryTest() : JpaContextTest() {

    @Autowired
    private lateinit var emailJpaRepository: EmailJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val emailRepositoryImpl: EmailRepositoryImpl by lazy {
        EmailRepositoryImpl(emailJpaRepository)
    }

    @Test
    fun `이메일 조회에 성공해야 한다`() {
        val emailAddress = EmailProvider.buildNormalAddress()
        jpaDataGenerator.emailEntityData(emailAddress)

        val result = emailRepositoryImpl.read(emailAddress)

        assert(result != null)
        assert(result!!.emailAddress == emailAddress.address)
    }

    @Test
    fun `이메일 조회에 실패해야 한다`() {
        val emailAddress = EmailProvider.buildNormalAddress()
        jpaDataGenerator.emailEntityData(emailAddress)

        val wrongEmailAddress = EmailProvider.buildWrongEmailAddress()

        val result = emailRepositoryImpl.read(wrongEmailAddress)

        assert(result == null)
    }

    @Test
    fun `이메일이 존재 한다면 저장하지 않는다`() {
        val emailAddress = EmailProvider.buildNormalAddress()

        val oldEmail = jpaDataGenerator.emailEntityData(emailAddress)

        val newEmailAddress = EmailProvider.buildNormalAddress()

        emailRepositoryImpl.appendIfNotExists(newEmailAddress)
        val result = emailJpaRepository.findByAddress(newEmailAddress.address)

        assert(result.isPresent)
        assert(result.get().toEmail().emailId == oldEmail.emailId)
    }

    @Test
    fun `이메일이 존재하지 않는다면 저장한다`() {
        val emailAddress = EmailProvider.buildNormalAddress()

        emailRepositoryImpl.appendIfNotExists(emailAddress)

        val result = emailJpaRepository.findByAddress(emailAddress.address)
        assert(result.isPresent)
        assert(result.get().toEmail().emailAddress == emailAddress.address)
    }

    @Test
    fun `이메일을 이메일 아이디로 읽는다`() {
        val emailAddress = EmailProvider.buildNormalAddress()

        val email = jpaDataGenerator.emailEntityData(emailAddress)

        val result = emailRepositoryImpl.readById(email.emailId)

        assert(result != null)
        assert(result!!.emailId == email.emailId)
    }
}
