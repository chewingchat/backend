package org.chewing.v1.implementation

import org.chewing.v1.external.ExternalWebSocketClient
import org.springframework.stereotype.Component

@Component
class WebSocketProvider(
    private val externalWebSocketClient: ExternalWebSocketClient
) {
    fun connect(userId: String, sessionId: String) {
        externalWebSocketClient.connect(userId, sessionId)
    }

    fun readAll(userId: String): List<String> {
        return externalWebSocketClient.readAll(userId)
    }

    fun unConnect(userId: String, sessionId: String) {
        externalWebSocketClient.unConnect(userId, sessionId)
    }
}