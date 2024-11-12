package org.chewing.v1.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Configuration
class OpenAiConfig {
    @Value("\${openai.api.key}")
    private lateinit var openAiKey: String

    @Bean
    fun webClient(): WebClient = WebClient.builder()
        .baseUrl("https://api.openai.com") // OpenAI API의 기본 URL 설정
        .filter(addAuthorizationHeader()) // Authorization 헤더 필터 추가
        .filter(logRequest()) // 요청 로깅 필터 추가 (선택 사항)
        .build()
    private fun addAuthorizationHeader(): ExchangeFilterFunction = ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
        val modifiedRequest = ClientRequest.from(clientRequest)
            .header("Authorization", "Bearer $openAiKey")
            .build()
        Mono.just(modifiedRequest)
    }

    private fun logRequest(): ExchangeFilterFunction = ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
        Mono.just(clientRequest)
    }.andThen(
        ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
            Mono.just(clientResponse)
        },
    )
}
