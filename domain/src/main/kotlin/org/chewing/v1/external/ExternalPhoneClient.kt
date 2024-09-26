package org.chewing.v1.external

import org.chewing.v1.model.contact.PhoneNumber

interface ExternalPhoneClient {
    fun sendSms(phoneNumber: PhoneNumber, verificationCode: String)
}