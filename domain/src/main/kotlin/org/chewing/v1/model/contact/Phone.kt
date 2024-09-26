package org.chewing.v1.model.contact

import org.chewing.v1.model.auth.ValidationCode
import java.time.LocalDateTime

class Phone private constructor(
    val phoneId: String,
    val country: String,
    val number: String,
    val validationCode: ValidationCode,
    val isAuthorizedFirst: Boolean
) : Contact {
    companion object {
        fun of(
            phoneId: String,
            country: String,
            number: String,
            authorizedNumber: String,
            expiredTime: LocalDateTime,
            isAuthorized: Boolean
        ): Phone {
            return Phone(
                phoneId = phoneId,
                country = country,
                number = number,
                validationCode = ValidationCode.of(
                    authorizedNumber,
                    expiredTime
                ),
                isAuthorizedFirst = isAuthorized
            )
        }

        fun generate(country: String, number: String): Phone {
            return Phone(
                phoneId = "",
                country = country,
                number = number,
                validationCode = ValidationCode.empty(),
                isAuthorizedFirst = false
            )
        }
    }
    override val type: ContactType
        get() = ContactType.PHONE
}
