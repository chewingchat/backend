package org.chewing.v1.util

import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

@Component
class CustomHandshakeHandler(
    private val jwtTokenProvider: JwtTokenProvider
) : DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal? {
        // HandshakeInterceptor에서 이미 Principal을 attributes에 추가했으므로 이를 반환
        return attributes["user"] as? Principal
    }
}
