package org.chewing.v1.repository

import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Phone

interface PhoneRepository {
    fun appendIfNotExists(phoneNumber: PhoneNumber)
    fun read(phoneNumber: PhoneNumber): Phone?
    fun updateVerificationCode(phoneNumber: PhoneNumber): String
    fun readById(phoneNumberId: String): Phone?
}