package org.chewing.v1.dto.request.chat

data class ChatLogRequest(
    val chatRoomId: String,
    val page: Int
)