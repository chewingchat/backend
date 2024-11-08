package org.chewing.v1.external

import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.springframework.stereotype.Component

@Component
class ExternalAuthClientImpl : ExternalAuthClient {
    override fun sendEmail(emailAddress: EmailAddress, verificationCode: String) {
        println("Sending email to $emailAddress")
    }

    override fun sendSms(phoneNumber: PhoneNumber, verificationCode: String) {
        println("Sending SMS to $phoneNumber")
    }
}
