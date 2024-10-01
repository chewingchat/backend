package org.chewing.v1.dto.response.user

import org.chewing.v1.model.user.UserProfile

data class UserProfileResponse(
    val firstName: String,
    val lastName: String,
    val birth: String,
    val phoneNumber: String,
    val countryCode: String,
    val emailAddress: String,
) {
    companion object {
        fun of(
            userProfile: UserProfile
        ): UserProfileResponse {
            return UserProfileResponse(
                firstName = userProfile.user.name.firstName(),
                lastName = userProfile.user.name.lastName(),
                birth = userProfile.user.birth,
                phoneNumber = userProfile.number,
                countryCode = userProfile.countryCode,
                emailAddress = userProfile.emailAddress
            )
        }
    }
}