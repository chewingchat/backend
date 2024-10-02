package org.chewing.v1.repository

import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Phone

interface PhoneRepository {
    fun savePhoneNumberIfNotExists(phoneNumber: PhoneNumber)
    fun readPhone(phoneNumber: PhoneNumber): Phone?
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String
    fun readPhoneByPhoneNumberId(phoneNumberId: String): Phone?
}