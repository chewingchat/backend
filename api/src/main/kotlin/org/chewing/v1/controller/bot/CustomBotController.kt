package org.chewing.v1.controller.bot

import org.chewing.v1.dto.request.ai.ChatGPTRequest
import org.chewing.v1.dto.response.ai.ChatGPTResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

@RestController
@RequestMapping("/bot")
class CustomBotController(
    private val template: RestTemplate
) {
    @Value("\${openai.model}")
    private lateinit var model: String

    @Value("\${openai.api.url}")
    private lateinit var apiURL: String

    @GetMapping("/chat")
    fun chat(@RequestParam(name = "prompt") prompt: String): String? {
        println("API URL: $apiURL")  // 디버깅용 로그 추가
        println("Model: $model")     // 디버깅용 로그 추가
        val request = ChatGPTRequest(model, prompt)
        val chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse::class.java)
        return chatGPTResponse?.choices?.get(0)?.message?.content
    }
}
