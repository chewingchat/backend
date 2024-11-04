package org.chewing.v1.dto


data class ChatGptMessage(
    var role: String = "",
    var content: String = ""
)