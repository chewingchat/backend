package org.chewing.v1.config

import org.chewing.v1.util.StompHandshakeInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
@Profile("!test")
class WebSocketConfig(
    private val stompHandshakeInterceptor: StompHandshakeInterceptor,
) : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        // 메시지 브로커의 경로 설정, /user만 포함
        config.enableSimpleBroker("/user") // 사용자별 메시지 전송을 위한 경로만 사용
        config.setUserDestinationPrefix("/user") // 특정 사용자 경로의 prefix 설정

        // 클라이언트가 서버에 메시지를 보낼 때 사용하는 기본 경로 prefix 설정
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // WebSocket 연결 엔드포인트 등록
        registry.addEndpoint("/ws-stomp")
            .setAllowedOrigins("*") // 모든 출처 허용
            .addInterceptors(stompHandshakeInterceptor)
            .withSockJS() // SockJS 지원 추가
        registry.addEndpoint("/ws-stomp")
            .setAllowedOrigins("*") // 모든 출처 허용
            .addInterceptors(stompHandshakeInterceptor)
    }
}