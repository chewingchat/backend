package org.chewing.v1.external

interface ExternalWebSocketClient {
    fun connect(userId: String, sessionId: String)
    fun readAll(userId: String): List<String>
    fun unConnect(userId: String, sessionId: String)
}