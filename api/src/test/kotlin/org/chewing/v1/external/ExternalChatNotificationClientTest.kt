package org.chewing.v1.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.chewing.v1.TestDataFactory
import org.chewing.v1.config.SecurityConfig
import org.chewing.v1.config.WebSocketConfig
import org.chewing.v1.implementation.session.SessionProvider
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.Thread.sleep
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WebSocketConfig::class, SecurityConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ExternalChatNotificationClientTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val externalChatNotificationClient: ExternalChatNotificationClient,
    @Autowired private val sessionProvider: SessionProvider
) {

    @LocalServerPort
    private var port: Int = 0
    private lateinit var latch: CountDownLatch
    private lateinit var session: StompSession
    private val objectMapper = ObjectMapper()
    private val userId = "testUserId"
    private lateinit var token: String
    private val chatMessages: ConcurrentLinkedQueue<ChatMessageDto> = ConcurrentLinkedQueue()


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

    @BeforeAll
    fun setup() {
        latch = CountDownLatch(8)
        // JWT 토큰 생성
        token = jwtTokenProvider.createAccessToken(userId)
        // STOMP 세션 연결
        session = connectStompSession()
        // 공통 구독 설정
        session.subscribe("/user/queue/chat", object : StompFrameHandler {
            override fun getPayloadType(headers: StompHeaders): Type {
                return ByteArray::class.java
            }

            override fun handleFrame(headers: StompHeaders, payload: Any?) {
                val message = String(payload as ByteArray, StandardCharsets.UTF_8)
                val chatMessage = objectMapper.readValue(message, ChatMessageDto::class.java)
                chatMessages.add(chatMessage)
                latch.countDown()
            }
        })
    }

    @AfterAll
    fun tearDown() {
        if (::session.isInitialized) {
            session.disconnect()
        }
    }


    @Test
    fun `채팅 메시지 전송`() {
        val testMessageId1 = "testMessageId1"
        val testMessageId2 = "testMessageId2"
        val testMessageId3 = "testMessageId3"
        val testMessageId4 = "testMessageId4"
        val testMessageId5 = "testMessageId5"
        val testMessageId6 = "testMessageId6"
        val testMessageId8 = "testMessageId8"
        val testChatRoomId1 = "testChatRoomId1"
        val testChatRoomId2 = "testChatRoomId2"
        val testChatRoomId3 = "testChatRoomId3"
        val testChatRoomId4 = "testChatRoomId4"
        val testChatRoomId5 = "testChatRoomId5"
        val testChatRoomId6 = "testChatRoomId6"
        val testChatRoomId7 = "testChatRoomId7"
        val testChatRoomId8 = "testChatRoomId8"

        // given
        val normalMessage = TestDataFactory.createNormalMessage(testMessageId1, testChatRoomId1)
        val inviteMessage = TestDataFactory.createInviteMessage(testMessageId2, testChatRoomId2)
        val fileMessage = TestDataFactory.createFileMessage(testMessageId3, testChatRoomId3)
        val deleteMessage = TestDataFactory.createDeleteMessage(testMessageId4, testChatRoomId4)
        val readMessage = TestDataFactory.createReadMessage(testChatRoomId7)
        val replyMessage = TestDataFactory.createReplyMessage(testMessageId5, testChatRoomId5)
        val bombMessage = TestDataFactory.createBombMessage(testMessageId6, testChatRoomId6)
        val leaveMessage = TestDataFactory.createLeaveMessage(testMessageId8, testChatRoomId8)

        sleep(1000)
        // 메시지 전송
        externalChatNotificationClient.sendMessage(normalMessage, userId)
        externalChatNotificationClient.sendMessage(inviteMessage, userId)
        externalChatNotificationClient.sendMessage(fileMessage, userId)
        externalChatNotificationClient.sendMessage(deleteMessage, userId)
        externalChatNotificationClient.sendMessage(readMessage, userId)
        externalChatNotificationClient.sendMessage(replyMessage, userId)
        externalChatNotificationClient.sendMessage(bombMessage, userId)
        externalChatNotificationClient.sendMessage(leaveMessage, userId)

        latch.await(10, TimeUnit.SECONDS)

        assertThat(chatMessages.size).isEqualTo(8)

        chatMessages.forEach { dto ->
            when (dto) {
                is ChatMessageDto.Bomb -> {
                    assertThat(dto.messageId).isEqualTo(testMessageId6)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId6)
                    assertThat(dto.type).isEqualTo("bomb")
                }

                is ChatMessageDto.Delete -> {
                    assertThat(dto.targetMessageId).isEqualTo(testMessageId4)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId4)
                    assertThat(dto.type).isEqualTo("delete")
                }

                is ChatMessageDto.File -> {
                    assertThat(dto.messageId).isEqualTo(testMessageId3)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId3)
                    assertThat(dto.type).isEqualTo("file")
                }

                is ChatMessageDto.Invite -> {
                    assertThat(dto.messageId).isEqualTo(testMessageId2)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId2)
                    assertThat(dto.type).isEqualTo("invite")
                }

                is ChatMessageDto.Leave -> {
                    assertThat(dto.messageId).isEqualTo(testMessageId8)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId8)
                    assertThat(dto.type).isEqualTo("leave")
                }

                is ChatMessageDto.Normal -> {
                    assertThat(dto.messageId).isEqualTo(testMessageId1)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId1)
                    assertThat(dto.type).isEqualTo("normal")
                }

                is ChatMessageDto.Read -> {
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId7)
                    assertThat(dto.type).isEqualTo("read")
                }

                is ChatMessageDto.Reply -> {
                    assertThat(dto.messageId).isEqualTo(testMessageId5)
                    assertThat(dto.chatRoomId).isEqualTo(testChatRoomId5)
                    assertThat(dto.type).isEqualTo("reply")
                }
            }
        }
    }
}
