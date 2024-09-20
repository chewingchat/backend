package org.chewing.v1.model

import org.chewing.v1.model.User

interface SignupRequest {
    fun toUser(): User
}