package org.chewing.v1.util

import mu.KotlinLogging
import org.chewing.v1.implementation.WebSocketProvider
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

private val logger = KotlinLogging.logger { }

@Component
class WebSocketEventListener(
    private val webSocketProvider: WebSocketProvider
) {

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId ?: return
        val userId = accessor.sessionAttributes?.get("userId") as? String ?: return

        webSocketProvider.connect(userId, sessionId)
        logger.info("세션 연결됨: userId=$userId, sessionId=$sessionId")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId ?: return
        val userId = accessor.sessionAttributes?.get("userId") as? String ?: return

        webSocketProvider.unConnect(userId, sessionId)
        logger.info("세션 해제됨: userId=$userId, sessionId=$sessionId")
    }
}
