package org.chewing.v1.dto.response.ai

import org.chewing.v1.dto.ChatGptMessage

// ChatGPTResponse

data class ChatGPTResponse(
    var choices: List<Choice> = listOf()
) {
    data class Choice(
        var index: Int = 0,
        var message: ChatGptMessage = ChatGptMessage()
    )
}
