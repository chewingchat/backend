package org.chewing.v1.model.contact

import org.chewing.v1.model.ValidationCode
import java.time.LocalDateTime


class Email private constructor(
    val emailId: String,
    val emailAddress: String,
    val validationCode: ValidationCode,
    val isAuthorizedFirst: Boolean
) : Contact {
    companion object {
        fun of(
            emailId: String,
            emailAddress: String,
            authorizedNumber: String,
            expiredTime: LocalDateTime,
            isAuthorized: Boolean
        ): Email {
            return Email(
                emailId = emailId,
                emailAddress = emailAddress,
                validationCode = ValidationCode.of(authorizedNumber, expiredTime),
                isAuthorizedFirst = isAuthorized
            )
        }

        fun generate(emailAddress: String): Email {
            return Email(
                emailId = "",
                emailAddress = emailAddress,
                validationCode = ValidationCode.empty(),
                isAuthorizedFirst = false
            )
        }
    }

    fun generateValidationCode(): Email {
        return Email(
            emailId = emailId,
            emailAddress = emailAddress,
            validationCode = ValidationCode.generate(),
            isAuthorizedFirst = isAuthorizedFirst
        )
    }

    override val type: ContactType
        get() = ContactType.EMAIL
}