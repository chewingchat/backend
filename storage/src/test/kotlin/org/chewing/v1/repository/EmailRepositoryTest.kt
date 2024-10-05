package org.chewing.v1.repository

import org.chewing.v1.repository.support.TestDataGenerator
import org.chewing.v1.config.DbContextTest
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jparepository.EmailJpaRepository
import org.junit.jupiter.api.Test
import org.chewing.v1.repository.support.EmailProvider
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

internal class EmailRepositoryTest(
) : DbContextTest() {

    @Autowired
    private lateinit var emailJpaRepository: EmailJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator


    private val emailRepositoryImpl: EmailRepositoryImpl by lazy {
        EmailRepositoryImpl(emailJpaRepository)
    }

    @Test
    fun `이메일 조회에 성공해야 한다`() {
        val emailAddress = EmailProvider.buildNormalAddress()
        testDataGenerator.emailEntityData(emailAddress)

        val result = emailRepositoryImpl.read(emailAddress)

        assert(result != null)
        assert(result!!.emailAddress == emailAddress.address)
    }

    @Test
    fun `이메일 조회에 실패해야 한다`() {
        val emailAddress = EmailProvider.buildNormalAddress()
        testDataGenerator.emailEntityData(emailAddress)

        val wrongEmailAddress = EmailProvider.buildWrongEmailAddress()

        val result = emailRepositoryImpl.read(wrongEmailAddress)

        assert(result == null)
    }

    @Test
    fun `이메일이 존재 한다면 저장하지 않는다`() {
        val emailAddress = EmailProvider.buildNormalAddress()

        val oldEmail = testDataGenerator.emailEntityData(emailAddress)

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

        val email = testDataGenerator.emailEntityData(emailAddress)

        val result = emailRepositoryImpl.readById(email.emailId)

        assert(result != null)
        assert(result!!.emailId == email.emailId)
    }

    @Test
    fun `이메일 인증번호를 업데이트 한다`() {
        val emailAddress = EmailProvider.buildNormalAddress()

        val email = testDataGenerator.emailEntityData(emailAddress)

        val result = emailRepositoryImpl.updateVerificationCode(emailAddress)

        assert(result != email.validationCode.code)
    }

    @Test
    fun `잘못된 접근으로 이메일 인증번호를 업데이트 할 수 없다`() {
        val emailAddress = EmailProvider.buildNormalAddress()
        val exception = assertThrows<NotFoundException> {
            emailRepositoryImpl.updateVerificationCode(emailAddress)
        }
        assert(exception.errorCode == ErrorCode.EMAIL_NOT_FOUND)
    }
}