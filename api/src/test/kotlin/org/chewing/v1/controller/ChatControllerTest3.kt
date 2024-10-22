package org.chewing.v1.controller

import org.chewing.v1.config.SecurityConfig
import org.chewing.v1.config.WebConfig
import org.chewing.v1.dto.request.chat.message.*
import org.chewing.v1.facade.ChatFacade
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WebConfig::class, SecurityConfig::class)
class ChatControllerTest3(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
) {

    @LocalServerPort
    private var port: Int = 0

    private val stompClient: WebSocketStompClient by lazy {
        WebSocketStompClient(StandardWebSocketClient()).apply {
            messageConverter = MappingJackson2MessageConverter()
        }
    }


    @Test
    fun `Authorization 헤더 없이 연결 시도 - 실패해야 함`() {
        val url = "ws://localhost:$port/ws-stomp"
        val webSocketHeaders = WebSocketHttpHeaders()
        val futureSession = stompClient.connectAsync(url, webSocketHeaders, object : StompSessionHandlerAdapter() {})

        val exception = assertThrows<ExecutionException> {
            futureSession.get(1, TimeUnit.MINUTES)
        }
        println(exception)
    }
}