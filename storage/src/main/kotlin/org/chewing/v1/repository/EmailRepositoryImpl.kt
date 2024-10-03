package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jparepository.EmailJpaRepository
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Email
import org.springframework.stereotype.Repository

@Repository
internal class EmailRepositoryImpl(
    private val emailJpaRepository: EmailJpaRepository,
):EmailRepository {
    override fun appendIfNotExists(emailAddress: EmailAddress) {
        emailJpaRepository.findByAddress(emailAddress.email)
            .orElseGet { emailJpaRepository.save(EmailJpaEntity.generate(emailAddress)) }
    }

    override fun read(emailAddress: EmailAddress): Email? {
        return emailJpaRepository.findByAddress(emailAddress.email).map { it.toEmail() }.orElse(null)
    }

    override fun updateVerificationCode(emailAddress: EmailAddress): String {
        val emailEntity = emailJpaRepository.findByAddress(emailAddress.email)
            .orElseThrow { NotFoundException(ErrorCode.EMAIL_NOT_FOUND) }
        emailEntity.updateVerificationCode()
        emailJpaRepository.save(emailEntity)
        return emailEntity.getAuthorizedNumber()
    }

    override fun readById(emailId: String): Email? {
        return emailJpaRepository.findByEmailId(emailId).map { it.toEmail() }.orElse(null)
    }
}