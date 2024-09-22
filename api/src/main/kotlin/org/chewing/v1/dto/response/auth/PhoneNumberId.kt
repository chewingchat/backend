package org.chewing.v1.dto.response.auth

data class PhoneNumberId(
    val phoneNumberId: String
) {
    companion object {
        fun of(phoneNumberId: String): PhoneNumberId {
            return PhoneNumberId(
                phoneNumberId = phoneNumberId
            )
        }
    }
}