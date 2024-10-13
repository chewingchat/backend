package org.chewing.v1.dto.request.chat.message

data class ChatReplyDto(
    val chatRoomId: String = "",
    val parentMessageId: String = "",
    val message: String = "",
) {
}