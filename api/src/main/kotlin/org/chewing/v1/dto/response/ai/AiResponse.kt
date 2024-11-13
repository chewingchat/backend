package org.chewing.v1.dto.response.ai

data class AiResponse(
    val promptResult: String,
) {
    companion object {
        fun of(promptResult: String): AiResponse = AiResponse(promptResult)
    }
}
