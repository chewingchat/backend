package org.chewing.v1.dto.request

data class EmailVerificationCheckRequest(
    val email: String,
    val verificationCode: String // 추가된 필드
)