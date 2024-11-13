package org.chewing.v1.external

// ChatGPTResponse

data class ChatGPTResponse(
    var choices: List<Choice> = listOf(),
) {
    data class Choice(
        var index: Int = 0,
        var message: ChatGptMessage,
    ) {
        data class ChatGptMessage(
            var role: String,
            var content: String,
        )
    }
}
