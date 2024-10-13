package org.chewing.v1.dto.request.chat.message

data class ChatDeleteDto(
    val chatRoomId: String = "",
    val messageId: String = "",
) {
}