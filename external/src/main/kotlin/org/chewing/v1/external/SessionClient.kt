package org.chewing.v1.external

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class SessionClient {

    private val cache: Cache<String, MutableList<String>> = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .maximumSize(100)
        .build()

    fun get(key: String): List<String> {
        return cache.getIfPresent(key) ?: emptyList()
    }

    fun put(key: String, value: String) {
        val values = cache.getIfPresent(key) ?: mutableListOf()
        values.add(value)
        cache.put(key, values)
    }

    fun remove(key: String) {
        cache.invalidate(key)
    }

    fun removeValue(key: String, value: String) {
        cache.getIfPresent(key)?.remove(value)
    }
}
