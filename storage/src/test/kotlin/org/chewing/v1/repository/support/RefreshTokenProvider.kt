package org.chewing.v1.repository.support

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
object RefreshTokenProvider {
    fun buildNormal(): RefreshToken {
        return RefreshToken.of("normal", LocalDateTime.now())
    }
    fun buildNew(): RefreshToken {
        return RefreshToken.of("new", LocalDateTime.now())
    }
}