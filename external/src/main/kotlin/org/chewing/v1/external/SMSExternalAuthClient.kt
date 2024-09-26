package org.chewing.v1.external

import org.chewing.v1.model.contact.PhoneNumber
import org.springframework.stereotype.Component

@Component
class SMSExternalAuthClient : ExternalPhoneClient {
    override fun sendSms(phoneNumber: PhoneNumber, verificationCode: String) {
        println("Sending SMS to $phoneNumber")
    }
}