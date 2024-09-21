package org.chewing.v1.model.contact

class PhoneNumber(
    val countryCode: String,
    val number: String
) {
    companion object {
        fun of(
            countryCode: String,
            number: String
        ): PhoneNumber {
            return PhoneNumber(
                countryCode = countryCode,
                number = number
            )
        }
    }
}
