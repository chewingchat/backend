package org.chewing.v1.external

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Component
class SessionClient {

    private val sessions = ConcurrentHashMap<String, MutableSet<String>>()

    fun addSession(userId: String, sessionId: String) {
        sessions.computeIfAbsent(userId) { ConcurrentHashMap.newKeySet() }.add(sessionId)
    }

    fun removeSession(userId: String, sessionId: String) {
        sessions[userId]?.remove(sessionId)
        if (sessions[userId]?.isEmpty() == true) {
            sessions.remove(userId)
        }
    }


    fun isUserOnline(userId: String): Boolean {
        return sessions.containsKey(userId)
    }

    fun getSessionId(userId: String): String {
        return sessions[userId]?.firstOrNull() ?: ""
    }
}
