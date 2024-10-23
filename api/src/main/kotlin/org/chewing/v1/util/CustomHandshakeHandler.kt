package org.chewing.v1.util

import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServletServerHttpRequest
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
        val servletRequest = (request as ServletServerHttpRequest).servletRequest
        val token = servletRequest.getHeader("Authorization")?.substringAfter("Bearer ")

        if (token != null) {
            try {
                val userId = jwtTokenProvider.getUserIdFromToken(token)
                return StompPrincipal(userId)
            } catch (e: Exception) {
                logger.error { "Failed to determine user ${e.message}" }
                return null
            }
        }
        logger.warn { "Authorization header missing" }
        return null
    }
}

class StompPrincipal(private val name: String) : Principal {
    override fun getName(): String = name
}
