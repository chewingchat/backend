package org.chewing.v1.util

import mu.KotlinLogging
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Component
class StompHandshakeInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
) : HandshakeInterceptor {
    private val logger = KotlinLogging.logger { }
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        logger.info { "beforeHandshake" }
        val servletRequest = (request as ServletServerHttpRequest).servletRequest
        val token = servletRequest.getHeader("Authorization")?.substringAfter("Bearer ")

        if (token != null) {
            try {
                val userId = jwtTokenProvider.getUserIdFromToken(token)
                attributes["userId"] = userId
                return true
            } catch (e: Exception) {
                return false
            }
        }
        return false
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        logger.info { "afterHandshake" }
    }

}