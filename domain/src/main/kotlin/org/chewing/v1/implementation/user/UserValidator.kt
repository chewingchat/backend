package org.chewing.v1.implementation.user

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.ActivateType
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class UserValidator {
    fun isUserActivated(user: User) {
        if (user.type != ActivateType.ACTIVATED) {
            throw ConflictException(ErrorCode.USER_NOT_ACTIVATED)
        }
    }
}