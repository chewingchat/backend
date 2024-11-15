package org.chewing.v1.external

import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KotlinLogging
import org.chewing.v1.dto.ChatGPTRequest
import org.chewing.v1.dto.ChatGPTResponse
import org.chewing.v1.model.ai.Prompt
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ExternalAiClientImpl(
    private val webClient: WebClient,
    @Value("\${openai.model}") private val model: String,

    @Value("\${openai.api.url}") private val apiURL: String,
) : ExternalAiClient {

    private val logger = KotlinLogging.logger {}

    override suspend fun prompt(prompts: List<Prompt>): String {
        val message = ChatGPTRequest.Message(
            content = prompts.map {
                ChatGPTRequest.Message.of(it)
            },
        )
        val request = ChatGPTRequest(model, listOf(message))

        return try {
            val responseBody: ChatGPTResponse? =
                webClient.post().uri(apiURL).contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve()
                    .bodyToMono(ChatGPTResponse::class.java).awaitSingleOrNull()

            if (responseBody != null && responseBody.choices.isNotEmpty()) {
                responseBody.choices[0].message.content.trim()
            } else {
                "요약을 생성할 수 없습니다."
            }
        } catch (e: Exception) {
            logger.error("API 호출 실패: ${e.message}", e)
            "API 호출 실패: ${e.message}"
        }
    }
}
