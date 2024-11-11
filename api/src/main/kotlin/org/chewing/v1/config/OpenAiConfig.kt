package org.chewing.v1.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate

@Configuration
class OpenAiConfig {
    @Value("\${openai.api.key}")
    private lateinit var openAiKey: String

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        val interceptor = ClientHttpRequestInterceptor { request, body, execution ->
            println("Using API Key: $openAiKey")  // 디버깅용 로그 추가
            request.headers.add("Authorization", "Bearer $openAiKey")
            execution.execute(request, body)
        }
        restTemplate.interceptors.add(interceptor)
        return restTemplate
    }
}
