package org.chewing.v1.repository.support

import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Phone
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
object PhoneProvider {
    fun buildNormalPhoneNumber(): PhoneNumber {
        return PhoneNumber.of("normal", "normal")
    }

    fun buildWrongPhoneNumber(): PhoneNumber {
        return PhoneNumber.of("wrong", "wrong")
    }

    fun buildNormal(): Phone {
        return Phone.of("normal", "normal", "normal", "normal", LocalDateTime.now())
    }
}
