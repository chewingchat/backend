package org.chewing.v1.external

import org.springframework.stereotype.Component

@Component
class ExternalWebSocketClientImpl(
    private val localCacheManager: LocalCacheManager
): ExternalWebSocketClient {
    override fun connect(userId: String, sessionId: String){
        localCacheManager.put(userId, sessionId)
    }
    override fun readAll(userId: String): List<String> {
        return localCacheManager.get(userId)
    }

    override fun unConnect(userId: String, sessionId: String) {
        localCacheManager.removeValue(userId, sessionId)
    }
}