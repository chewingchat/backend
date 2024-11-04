package org.chewing.v1.dto.request.ai

import org.chewing.v1.dto.ChatGptMessage

data class ChatGPTRequest(
    var model: String,
    var messages: MutableList<ChatGptMessage> = mutableListOf()
) {
    constructor(model: String, prompt: String) : this(model) {
        messages.add(ChatGptMessage("user", prompt))
    }
}