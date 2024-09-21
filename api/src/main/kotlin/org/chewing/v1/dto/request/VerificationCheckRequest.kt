package org.chewing.v1.dto.request

import org.chewing.v1.model.contact.PhoneNumber

class VerificationCheckRequest {
    data class Email(
        val email: String,
        val verificationCode: String
    ){
        fun toAddress(): String {
            return email
        }

        fun toVerificationCode(): String {
            return verificationCode
        }
    }
    data class Phone(
        val phoneNumber: String,
        val countryCode: String,
        val verificationCode: String
    ){
        fun toPhoneNumber(): PhoneNumber {
            return PhoneNumber.of(phoneNumber, countryCode)
        }

        fun toCountryCode(): String {
            return countryCode
        }

        fun toVerificationCode(): String {
            return verificationCode
        }
    }
}