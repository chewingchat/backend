package org.chewing.v1.dto.request

data class PhoneVerificationRequest(
    val phoneNumber: String,
    val countryCode: String
)