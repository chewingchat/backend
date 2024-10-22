package org.chewing.v1.implementation

import org.chewing.v1.external.ExternalSessionClient
import org.springframework.stereotype.Component

@Component
class SessionProvider(
    private val externalSessionClient: ExternalSessionClient
) {
    fun connect(userId: String, sessionId: String) {
        externalSessionClient.connect(userId, sessionId)
    }

    fun readAll(userId: String): List<String> {
        return externalSessionClient.readAll(userId)
    }

    fun unConnect(userId: String, sessionId: String) {
        externalSessionClient.unConnect(userId, sessionId)
    }
}