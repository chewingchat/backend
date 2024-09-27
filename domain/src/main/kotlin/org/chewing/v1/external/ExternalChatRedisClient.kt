package org.chewing.v1.external

import org.chewing.v1.model.ChatMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
class ExternalChatRedisClient(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val channelTopic: ChannelTopic,
    private val messagingTemplate: SimpMessageSendingOperations
) {
    fun sendMessage(message: ChatMessage) {
        redisTemplate.convertAndSend(channelTopic.topic, message)
        messagingTemplate.convertAndSend("/topic/${message.roomId}", message)
    }
}