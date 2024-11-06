package org.chewing.v1.external

import org.springframework.stereotype.Component

@Component
class ExternalSessionClientImpl(
    private val sessionClient: SessionClient
) : ExternalSessionClient {
    override fun connect(userId: String, sessionId: String) {
        sessionClient.addSession(userId, sessionId)
    }
    override fun isOnline(userId: String): Boolean {
        return sessionClient.isUserOnline(userId)
    }

    override fun getSessionId(userId: String): String {
        return sessionClient.getSessionId(userId)
    }

    override fun unConnect(userId: String, sessionId: String) {
        sessionClient.removeSession(userId, sessionId)
    }
}
