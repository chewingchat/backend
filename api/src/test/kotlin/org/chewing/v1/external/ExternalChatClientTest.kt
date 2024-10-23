package org.chewing.v1.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.chewing.v1.config.SecurityConfig
import org.chewing.v1.config.WebConfig
import org.chewing.v1.config.WebSocketConfig
import org.chewing.v1.implementation.SessionProvider
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.message.ChatNormalMessage
import org.chewing.v1.model.chat.room.ChatNumber
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.Thread.sleep
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WebSocketConfig::class, SecurityConfig::class)
class ExternalChatClientTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val externalChatClient: ExternalChatClient,
    @Autowired private val sessionProvider: SessionProvider
) {

    @LocalServerPort
    private var port: Int = 0
    private lateinit var latch: CountDownLatch
    private val objectMapper = ObjectMapper()
    private val userId = "testUserId"
    private val token = jwtTokenProvider.createAccessToken(userId)


    private val stompClient: WebSocketStompClient by lazy {
        WebSocketStompClient(StandardWebSocketClient()).apply {
            messageConverter = MappingJackson2MessageConverter()
        }
    }

    private fun connectStompSession(): StompSession {
        val headers = WebSocketHttpHeaders().apply {
            set("Authorization", "Bearer $token")
        }
        val url = "ws://localhost:$port/ws-stomp"

        // CompletableFuture 사용
        val futureSession = stompClient.connectAsync(url, headers, object : StompSessionHandlerAdapter() {
        })
        return futureSession.get(1, TimeUnit.MINUTES) // 연결이 완료될 때까지 최대 1분 대기
    }

    @Test
    fun `send message`() {
        latch = CountDownLatch(1)

        // given
        val chatMessage = buildNormalMessage("testMessageId", "testChatRoomId")

        // WebSocket 세션 연결
        val session = connectStompSession()

        var testMessage: TestMessage? = null

        session.subscribe("/user/queue/chat/private", object : StompFrameHandler {
            override fun getPayloadType(headers: StompHeaders): Type {
                return  Any::class.java
            }

            override fun handleFrame(headers: StompHeaders, payload: Any?) {
                val message = String(payload as ByteArray, StandardCharsets.UTF_8)
                testMessage = objectMapper.readValue(message, TestMessage::class.java)
                latch.countDown()
            }
        })

        sleep(1000)
        // 메시지 전송
        externalChatClient.sendMessage(chatMessage, userId)


        latch.await(10, TimeUnit.MINUTES)
        assertThat(testMessage).isNotNull()
        assertThat(testMessage?.message).isEqualTo("1")
    }

    // 테스트 메시지 생성
    fun buildNormalMessage(messageId: String, chatRoomId: String): ChatNormalMessage {
        return ChatNormalMessage.of(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = "sender",
            text = "text",
            number = ChatNumber.of(chatRoomId, 1, 1),
            timestamp = LocalDateTime.now(),
        )
    }
}
