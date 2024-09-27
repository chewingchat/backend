package org.chewing.v1.config


import org.chewing.v1.external.ExternalChatRedisClient
import org.chewing.v1.external.ExternalImageClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter


@Configuration
class RedisConfig(
    private val redisConnectionFactory: RedisConnectionFactory,

) {

    @Bean
    fun redisMessageListenerContainer(
        messageListenerAdapter: MessageListenerAdapter,
        channelTopic: ChannelTopic
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisConnectionFactory)
        container.addMessageListener(messageListenerAdapter, channelTopic)
        return container
    }

    @Bean
    fun messageListenerAdapter(externalChatRedisClient: ExternalChatRedisClient): MessageListenerAdapter {
        return MessageListenerAdapter(externalChatRedisClient, "receiveMessage")
    }

    @Bean
    fun channelTopic(): ChannelTopic {
        return ChannelTopic("chat")
    }
}