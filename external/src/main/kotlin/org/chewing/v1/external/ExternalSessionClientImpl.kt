package org.chewing.v1.external

import org.springframework.stereotype.Component

@Component
class ExternalSessionClientImpl(
    private val sessionClient: SessionClient
): ExternalSessionClient {
    override fun connect(userId: String, sessionId: String){
        sessionClient.put(userId, sessionId)
    }
    override fun readAll(userId: String): List<String> {
        return sessionClient.get(userId)
    }

    override fun unConnect(userId: String, sessionId: String) {
        sessionClient.removeValue(userId, sessionId)
    }
}