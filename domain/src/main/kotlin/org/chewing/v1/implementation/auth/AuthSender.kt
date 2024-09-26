package org.chewing.v1.implementation.auth

import org.chewing.v1.external.ExternalEmailClient
import org.chewing.v1.external.ExternalPhoneClient
import org.chewing.v1.model.contact.PhoneNumber
import org.springframework.stereotype.Component

@Component
class AuthSender(
    val externalPhoneClient: ExternalPhoneClient,
    val externalEmailClient: ExternalEmailClient
) {
    fun sendPhoneVerificationCode(phoneNumber: PhoneNumber, verificationCode: String) {
        externalPhoneClient.sendSms(phoneNumber, verificationCode)
    }

    fun sendEmailVerificationCode(emailAddress: String, verificationCode: String) {
        externalEmailClient.sendEmail(emailAddress, verificationCode)
    }
}