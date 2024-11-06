package org.chewing.v1.repository.auth

import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jparepository.auth.EmailJpaRepository
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Email
import org.springframework.stereotype.Repository

@Repository
internal class EmailRepositoryImpl(
    private val emailJpaRepository: EmailJpaRepository,
) : EmailRepository {
    override fun appendIfNotExists(emailAddress: EmailAddress): String {
        return emailJpaRepository.findByAddress(emailAddress.address).map {
            it.updateVerificationCode()
            emailJpaRepository.save(it)
            it.getAuthorizedNumber()
        }.orElseGet {
            val newEmailEntity = EmailJpaEntity.generate(emailAddress)
            emailJpaRepository.save(newEmailEntity)
            newEmailEntity.getAuthorizedNumber()
        }
    }
    override fun read(emailAddress: EmailAddress): Email? {
        return emailJpaRepository.findByAddress(emailAddress.address).map { it.toEmail() }.orElse(null)
    }

    override fun readById(emailId: String): Email? {
        return emailJpaRepository.findByEmailId(emailId).map { it.toEmail() }.orElse(null)
    }
}
