package org.chewing.v1.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.http.client.ClientHttpRequestInterceptor

@Configuration
class OpenAiConfig {
    @Value("\${openai.api.key}")
    private lateinit var openAiKey: String

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        val interceptor = ClientHttpRequestInterceptor { request, body, execution ->
            request.headers.add("Authorization", "Bearer $openAiKey")
            execution.execute(request, body)
        }
        restTemplate.interceptors.add(interceptor)
        return restTemplate
    }
}
