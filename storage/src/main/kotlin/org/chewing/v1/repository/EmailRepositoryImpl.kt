package org.chewing.v1.repository

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jparepository.EmailJpaRepository
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.springframework.stereotype.Repository

@Repository
internal class EmailRepositoryImpl(
    private val emailJpaRepository: EmailJpaRepository,
):EmailRepository {
    override fun saveEmailIfNotExists(emailAddress: EmailAddress) {
        emailJpaRepository.findByEmailAddress(emailAddress.email)
            .orElseGet { emailJpaRepository.save(EmailJpaEntity.generate(emailAddress)) }
    }

    override fun readEmail(emailAddress: EmailAddress): Contact? {
        return emailJpaRepository.findByEmailAddress(emailAddress.email).map { it.toEmail() }.orElse(null)
    }

    override fun updateEmailVerificationCode(emailAddress: EmailAddress): String {
        val emailEntity = emailJpaRepository.findByEmailAddress(emailAddress.email)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
        emailEntity.updateVerificationCode()
        emailJpaRepository.save(emailEntity)
        return emailEntity.getAuthorizedNumber()
    }

    override fun readEmailByEmailId(emailId: String): Email? {
        return emailJpaRepository.findByEmailId(emailId).map { it.toEmail() }.orElse(null)
    }
}