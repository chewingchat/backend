package org.chewing.v1.implementation.user

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.user.User
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserValidator(
) {
    fun isUserAccess(user: User) {
        if (user.status != AccessStatus.ACCESS) {
            throw ConflictException(ErrorCode.USER_NOT_ACCESS)
        }
    }
}