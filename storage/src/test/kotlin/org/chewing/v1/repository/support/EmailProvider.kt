package org.chewing.v1.repository.support

import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Email
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
object EmailProvider {
    fun buildNormalAddress(): EmailAddress {
        return EmailAddress.of("normal")
    }
    fun buildWrongEmailAddress(): EmailAddress {
        return EmailAddress.of("wrong")
    }
    fun buildNormal(): Email {
        return Email.of("normal", "normal", "normal", LocalDateTime.now())
    }
    fun buildNew(): Email {
        return Email.of("new", "new", "new", LocalDateTime.now())
    }
}
