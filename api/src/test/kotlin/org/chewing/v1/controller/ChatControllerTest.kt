package org.chewing.v1.controller

import org.chewing.v1.config.SecurityConfig
import org.chewing.v1.config.WebConfig
import org.chewing.v1.config.WebSocketConfig
import org.chewing.v1.dto.request.chat.message.*
import org.chewing.v1.facade.ChatFacade
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.junit.jupiter.api.*
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WebSocketConfig::class, WebConfig::class, SecurityConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ChatControllerTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
) {

    @MockBean
    private lateinit var chatFacade: ChatFacade

    @LocalServerPort
    private var port: Int = 0

    private val userId = "testUserId"
    private val token = jwtTokenProvider.createAccessToken(userId)
    private lateinit var session: StompSession

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
        session = connectStompSession()
    }


    @Test
    fun `일반 메세지 전송`() {
        val latch = CountDownLatch(1)
        doAnswer {
            latch.countDown()
            null
        }.whenever(chatFacade).processCommon(any(), any(), any())

        val chatDto = ChatCommonDto("testRoomId", "testUserId")
        session.send("/app/chat/common", chatDto)
        latch.await(1, TimeUnit.MINUTES)
        verify(chatFacade).processCommon(chatDto.chatRoomId, userId, chatDto.message)
    }

    @Test
    fun `읽기 메시지 전송`(){
        val latch = CountDownLatch(1)
        doAnswer {
            latch.countDown()
            null
        }.whenever(chatFacade).processRead(any(), any())

        val chatReadDto = ChatReadDto("testRoomId")
        session.send("/app/chat/read", chatReadDto)

        latch.await(1, TimeUnit.MINUTES)
        verify(chatFacade).processRead(chatReadDto.chatRoomId, userId)
    }

    @Test
    fun `삭제 메시지 전송`(){
        val latch = CountDownLatch(1)
        doAnswer {
            latch.countDown()
            null
        }.whenever(chatFacade).processDelete(any(), any(), any())

        val chatDeleteDto = ChatDeleteDto("testRoomId", "testMessageId")
        session.send("/app/chat/delete", chatDeleteDto)

        latch.await(1, TimeUnit.MINUTES)
        verify(chatFacade).processDelete(chatDeleteDto.chatRoomId, userId, chatDeleteDto.messageId)
    }

    @Test
    fun `답장 메시지 전송`(){
        val latch = CountDownLatch(1)
        doAnswer {
            latch.countDown()
            null
        }.whenever(chatFacade).processReply(any(), any(), any(), any())


        val chatReplyDto = ChatReplyDto("testRoomId", "testParentMessageId", "testMessage")
        session.send("/app/chat/reply", chatReplyDto)

        latch.await(1, TimeUnit.MINUTES)
        verify(chatFacade).processReply(chatReplyDto.chatRoomId, userId, chatReplyDto.parentMessageId, chatReplyDto.message)
    }

    @Test
    fun `폭탄 메시지 전송`(){
        val latch = CountDownLatch(1)
        doAnswer {
            latch.countDown()
            null
        }.whenever(chatFacade).processBombing(any(), any(), any(),any())


        val chatBombMessage = ChatBombDto("testRoomId", "testMessage", "2024:10:22 13:45:30")
        session.send("/app/chat/bomb", chatBombMessage)

        latch.await(1, TimeUnit.MINUTES)
        verify(chatFacade).processBombing(chatBombMessage.chatRoomId, userId, chatBombMessage.message, chatBombMessage.toExpireAt())
    }

}