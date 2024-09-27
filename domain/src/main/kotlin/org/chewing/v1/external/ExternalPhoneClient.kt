package org.chewing.v1.external

import org.chewing.v1.model.auth.PhoneNumber

interface ExternalPhoneClient {
    fun sendSms(phoneNumber: PhoneNumber, verificationCode: String)
}