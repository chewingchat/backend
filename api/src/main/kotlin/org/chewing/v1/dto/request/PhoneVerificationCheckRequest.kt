package org.chewing.v1.dto.request

data class PhoneVerificationCheckRequest (
    val phoneNumber: String,
    val countryCode: String,
    val verificationCode: String // 추가된 필드
)