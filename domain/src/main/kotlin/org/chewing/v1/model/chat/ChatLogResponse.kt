package org.chewing.v1.model.chat

import org.chewing.v1.model.*


data class ChatLogResponse(
    val page: Int,
    val messages: List<MessageResponse>
)