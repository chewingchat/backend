package org.chewing.v1.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.model.chat.message.ChatMessage
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component


@Component
class ExternalChatClientImpl(
    private val messagingTemplate: SimpMessagingTemplate,
    private val objectMapper: ObjectMapper
) : ExternalChatClient {
    override fun sendMessage(chatMessage: ChatMessage, userId: String) {

        val headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE)
        headerAccessor.setLeaveMutable(true)


        messagingTemplate.convertAndSendToUser(
            userId,
            "/queue/chat/private",
            TestMessage("1")
        )
    }
}