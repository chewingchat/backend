package org.chewing.v1.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class CacheConfig {
    // Redis 서버와 통신하고 데이터를 저장하거나 읽어오는 데 사용되는 기본 도구
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        // RedisConnectionFactory는 Redis 서버와의 연결을 관리하는 객체
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(connectionFactory)
        return template
    }
}
