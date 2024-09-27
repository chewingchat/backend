package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.external.ExternalEmailClient
import org.chewing.v1.external.ExternalPhoneClient
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.springframework.stereotype.Component

@Component
class AuthSender(
    val externalPhoneClient: ExternalPhoneClient,
    val externalEmailClient: ExternalEmailClient
) {

    fun sendVerificationCode(credential: Credential, verificationCode: String) {
        when (credential) {
            is PhoneNumber -> externalPhoneClient.sendSms(credential, verificationCode)
            is EmailAddress -> externalEmailClient.sendEmail(credential, verificationCode)
            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }
}