package org.chewing.v1.dto

import org.chewing.v1.model.ai.ImagePrompt
import org.chewing.v1.model.ai.Prompt
import org.chewing.v1.model.ai.TextPrompt

data class ChatGPTRequest(
    val model: String,
    val messages: List<Message>,
) {
    data class Message(
        val role: String = "user",
        val content: List<Any>,
    ) {
        companion object {
            fun of(prompt: Prompt): Any = when (prompt) {
                is TextPrompt -> ContentText.of(prompt)
                is ImagePrompt -> ContentImage.of(prompt)
            }
        }
        data class ContentText(
            val type: String = "text",
            val text: String,
        ) {
            companion object {
                fun of(prompt: TextPrompt): ContentText = ContentText(text = prompt.text)
            }
        }

        data class ContentImage(
            val type: String = "image_url",
            val image_url: Url,
        ) {
            companion object {
                fun of(prompt: ImagePrompt): ContentImage = ContentImage(image_url = Url(prompt.imageUrl))
            }

            data class Url(
                val url: String,
            )
        }
    }
}
