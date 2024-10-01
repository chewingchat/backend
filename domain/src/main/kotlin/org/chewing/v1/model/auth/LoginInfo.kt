package org.chewing.v1.model.auth

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.user.User

class LoginInfo private constructor(
    val jwtToken: JwtToken,
    val loginType: AccessStatus
) {
    companion object {
        fun of(jwtToken: JwtToken, user: User): LoginInfo {
            return LoginInfo(jwtToken, user.status)
        }
    }
}